plugins {
    kotlin("jvm")
    application
    id("com.github.johnrengelman.shadow")
}

application {
    mainClass.set("ru.otus.otuskotlin.marketplace.app.rabbit.ApplicationKt")
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-app-common"))

    // v1 api
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-mappers-v1"))

    // v2 api
    implementation(project(":ok-marketplace-mappers-v2"))
    implementation(project(":ok-marketplace-api-v2-kmp"))

    implementation(project(":ok-marketplace-biz"))
    implementation(project(":ok-marketplace-stubs"))

    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation(kotlin("test"))
}
