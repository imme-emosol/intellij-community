// Copyright 2000-2023 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package org.jetbrains.sqlite

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.util.SystemInfoRt
import com.intellij.openapi.util.io.NioFiles
import com.intellij.util.ResourceUtil
import com.intellij.util.io.DigestUtil
import com.intellij.util.system.CpuArch
import org.jetbrains.sqlite.core.Codes
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

private var extracted = false

// The version of the SQLite JDBC driver.
private const val VERSION: String = "3.40.0.0"

/**
 * Loads the SQLite interface backend.
 *
 * @return True if the SQLite JDBC driver is successfully loaded; false otherwise.
 */
@Synchronized
internal fun loadNativeDb() {
  if (extracted) {
    return
  }

  loadSqliteNativeLibrary()
  extracted = true
}

/**
 * Loads SQLite native library using given a path and name of the library.
 */
private fun loadSqliteNativeLibrary() {
  @Suppress("SpellCheckingInspection")
  var nativeLibraryName = System.mapLibraryName("sqlitejdbc")?.replace(".dylib", ".jnilib")!!
  val relativeDirName = "${osNameToDirName()}/${if (CpuArch.isArm64()) "aarch64" else "x86_64"}"
  val libPath = try {
    PathManager.getLibPath()
  }
  catch (ignore: RuntimeException) {
    // unit test or benchmark - no home path
    null
  }
  if (libPath != null) {
    val nativeLibFile = Path.of(libPath, "native", relativeDirName, nativeLibraryName).toAbsolutePath().normalize()
    if (Files.exists(nativeLibFile)) {
      System.load(nativeLibFile.toString())
      return
    }
  }

  // load the os-dependent library from the jar file
  val nativeLibraryPath = "sqlite-native/$relativeDirName"
  val classLoader = Codes::class.java.classLoader
  var hasNativeLib = classLoader.getResource("$nativeLibraryPath/$nativeLibraryName") != null
  if (!hasNativeLib && SystemInfoRt.isMac) {
    // fix for openjdk7 for Mac
    @Suppress("SpellCheckingInspection")
    val altName = "libsqlitejdbc.jnilib"
    if (classLoader.getResource("$nativeLibraryPath/$altName") != null) {
      nativeLibraryName = altName
      hasNativeLib = true
    }
  }

  if (hasNativeLib) {
    // try extracting the library from jar
    val tempDir = if (libPath == null) {
      val dir = Files.createTempDirectory("sqlite-")
      Runtime.getRuntime().addShutdownHook(Thread(Runnable { NioFiles.deleteRecursively(dir) }, "remove sqlite native temp dir"))
      dir
    }
    else {
      Path.of(PathManager.getTempPath())
    }
    extractAndLoadLibraryFile(libFolderForCurrentOS = nativeLibraryPath, libraryFileName = nativeLibraryName, tempDir = tempDir)
  }
  else {
    throw Exception("No native library found for os.name=${SystemInfoRt.OS_NAME}, os.arch=${CpuArch.CURRENT}")
  }
}

/**
 * Extracts and loads the specified library file to the target folder
 */
private fun extractAndLoadLibraryFile(libFolderForCurrentOS: String, libraryFileName: String, tempDir: Path) {
  val nativeLibraryFilePath = "$libFolderForCurrentOS/$libraryFileName"
  val classLoader = Codes::class.java.classLoader

  val expectedHash = ResourceUtil.getResourceAsBytes("$nativeLibraryFilePath.sha256", classLoader)!!.decodeToString()

  val extractedLibFileName = "sqlite-$VERSION-$libraryFileName"
  val targetDir = tempDir.resolve("sqlite-native").toAbsolutePath().normalize()
  val extractedLibFile = targetDir.resolve(extractedLibFileName)
  if (!Files.exists(extractedLibFile) || expectedHash != DigestUtil.sha256Hex(extractedLibFile)) {
    Files.createDirectories(targetDir)
    classLoader.getResourceAsStream(nativeLibraryFilePath)!!.use { reader ->
      Files.copy(reader, extractedLibFile, StandardCopyOption.REPLACE_EXISTING)
    }

    // verify
    val actualHash = DigestUtil.sha256Hex(extractedLibFile)
    if (expectedHash != actualHash) {
      throw RuntimeException("Failed to write a native library file at $extractedLibFile")
    }
  }
  System.load(extractedLibFile.toString())
}

private fun osNameToDirName(): String {
  return when {
    SystemInfoRt.isWindows -> "win"
    SystemInfoRt.isMac -> "mac"
    else -> "linux"
  }
}