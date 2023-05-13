plugins {
    kotlin("jvm")
}

val coroutinesVersion: String by project
val jacksonVersion: String by project
val okhttpVersion: String by project
val logbackVersion: String by project
val loggingVersion: String by project

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

//    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.github.microutils:kotlin-logging-jvm:$loggingVersion")

    implementation("com.squareup.okhttp3:okhttp:$okhttpVersion") // http client
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion") // from string to object

    testImplementation(kotlin("test-junit"))
}
