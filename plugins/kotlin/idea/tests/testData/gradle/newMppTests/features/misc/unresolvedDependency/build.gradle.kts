plugins {
    kotlin("multiplatform") version "{{kotlin_plugin_version}}"
}

repositories {
    {{kts_kotlin_plugin_repositories}}
}

kotlin {
    jvm() 
    ios()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("unresolved:unresolved:1.0")
            }
        }
    }
}
