plugins {
    kotlin("jvm")
}

dependencies {
    val testcontainersVersion: String by project
    val kotestVersion: String by project
    val ktorVersion: String by project
    val coroutinesVersion: String by project
    val logbackVersion: String by project
    val kotlinLoggingJvmVersion: String by project

    testImplementation(kotlin("stdlib"))

    testImplementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingJvmVersion")

    testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
    testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
    testImplementation("io.kotest:kotest-property:$kotestVersion")

    testImplementation("org.testcontainers:testcontainers:$testcontainersVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    testImplementation("io.ktor:ktor-client-core:$ktorVersion")
    testImplementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    testImplementation("io.ktor:ktor-client-okhttp-jvm:$ktorVersion")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()
    }
    test {
        systemProperty("kotest.framework.test.severity", "NORMAL")
    }
    create<Test>("test-strict") {
        systemProperty("kotest.framework.test.severity", "MINOR")
        group = "verification"
    }
}
