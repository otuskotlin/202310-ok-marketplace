import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

plugins {
    kotlin("jvm") apply false
    kotlin("multiplatform") apply false
}

val JVM_TAEGET = "17"

group = "ru.otus.otuskotlin.marketplace"
version = "0.0.1"

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = JVM_TAEGET
    }
    tasks.withType<KotlinJvmCompile> {
        kotlinOptions.jvmTarget = JVM_TAEGET
    }
}
