// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.io.pagecache;

import com.intellij.openapi.util.ThrowableNotNullFunction;
import com.intellij.util.io.FilePageCacheLockFree;

import java.io.Flushable;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Page: region of file, 'mapped' into memory. Part of our own file page cache
 * implementation, {@link com.intellij.util.io.PagedFileStorageLockFree}
 * <p>
 *
 * @see FilePageCacheLockFree
 */
public interface Page extends AutoCloseable, Flushable {
  int pageSize();

  int pageIndex();

  long offsetInFile();

  long lastOffsetInFile();

  void lockPageForWrite();

  void unlockPageForWrite();

  void lockPageForRead();

  void unlockPageForRead();

  boolean isUsable();

  void release();

  /** == {@link #release()} */
  @Override
  void close();

  boolean isDirty();

  @Override
  void flush() throws IOException;

  //=============================================================================================
  //RC: I plan lambda-based .read() and .write() methods to be the default option for accessing page
  //    content -- this is the safest way, since all locking, page state checking, buffer range
  //    checking/slicing, etc. is done once, under the cover and can't be messed up.
  //
  //    Pair of methods .dataUsafe() and .regionModified(), together with .lockForXXX()/.unlockForXXX()
  //    is the 'low-level API' -- no lambda overhead, no buffer slicing overhead, but you need to be
  //    very careful to put all important pieces in the right places. I.e. if you forget to call
  //    .regionModified() then your changes will be still available while the page is in cache,
  //    but will be lost as soon as the page is evicted and re-loaded later (good luck debugging).
  //
  //    Methods like .getXXX(), .putXXX() are supposed to be used in the least performance-critical
  //    places. They are here mostly for compatibility with previous PFCache implementation, to ease
  //    transition.


  <OUT, E extends Exception> OUT read(final int startOffsetOnPage,
                                      final int length,
                                      final ThrowableNotNullFunction<ByteBuffer, OUT, E> reader) throws E;

  <OUT, E extends Exception> OUT write(final int startOffsetOnPage,
                                       final int length,
                                       final ThrowableNotNullFunction<ByteBuffer, OUT, E> writer) throws E;

  //=============================================================================================
  // BEWARE: low-level & unsafe page data access methods:

  /**
   * Direct reference to internal buffer returned. This is an unsafe method to access the data. It is
   * the responsibility of the caller to ensure appropriate read/write lock is acquired, and
   * page is kept 'in use' (i.e. not .close()-ed) for all the period of using the returned buffer.
   * Returned buffer should be used only in 'absolute positioning' way, i.e. without any access to
   * buffer.position() and buffer.limit() cursors.
   * If caller modifies content of the returned buffer, the caller must inform page about
   * modifications via approriate {@link #regionModified(int, int)} call.
   */
  ByteBuffer rawPageBuffer();

  /**
   * Must be called only under page writeLock. To be used only with writes via {@link #rawPageBuffer()}
   * as a way to inform page about a buffer region that was really modified.
   */
  void regionModified(final int startOffsetModified,
                      final int length);

  //=============================================================================================


  byte get(final int offsetInPage);

  int getInt(final int offsetInPage);

  long getLong(final int offsetInPage);

  void readToArray(final byte[] destination,
                   final int offsetInArray,
                   final int offsetInPage,
                   final int length);

  void put(final int offsetInPage,
           final byte value);

  void putInt(final int offsetInPage,
              final int value);

  void putLong(final int offsetInPage,
               final long value);

  void putFromBuffer(final ByteBuffer data,
                     final int offsetInPage);

  void putFromArray(final byte[] source,
                    final int offsetInArray,
                    final int offsetInPage,
                    final int length);



  /**
   * More detailed page description than {@link #toString()}.
   * Exists only for debug purposes
   */
  String formatData();
}
