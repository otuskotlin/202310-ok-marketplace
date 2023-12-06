import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

plugins {
    kotlin("multiplatform")
    id("com.crowdproj.generator")
    kotlin("plugin.serialization")
}

crowdprojGenerate {
    packageName.set("${project.group}.api.v2")
    inputSpec.set("$rootDir/specs/specs-ad-v2.yaml")
}

kotlin {
    jvm { withJava() }
    linuxX64 { }
    macosX64 { }
    macosArm64 { }

    sourceSets {
        val serializationVersion: String by project
        val commonMain by getting {
            kotlin.srcDirs(layout.buildDirectory.dir("generate-resources/main/src/commonMain/kotlin"))
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}

tasks {
    val openApiGenerateTask: GenerateTask = getByName("openApiGenerate", GenerateTask::class) {
        outputDir.set(layout.buildDirectory.file("generate-resources/main/src/commonMain/kotlin").get().toString())
        mustRunAfter("compileCommonMainKotlinMetadata")
    }
    filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerateTask)
    }
    build {
        dependsOn("jvmJar")
    }
}
