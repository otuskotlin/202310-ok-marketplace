val jacksonVersion: String by project
val serializationVersion: String by project
val yandexCloudSdkVersion: String by project

plugins {
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
}

dependencies {
    implementation("com.yandex.cloud:java-sdk-functions:$yandexCloudSdkVersion")
    implementation(kotlin("stdlib-jdk8"))

    // transport models
    implementation(project(":ok-marketplace-common"))
    implementation(project(":ok-marketplace-api-v1-jackson"))
    implementation(project(":ok-marketplace-api-v2-kmp"))
    implementation(project(":ok-marketplace-mappers-v1"))
    implementation(project(":ok-marketplace-mappers-v2"))

    // Stubs
    implementation(project(":ok-marketplace-stubs"))
}
