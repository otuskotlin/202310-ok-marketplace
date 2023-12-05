group = "com.wizy"
version = "0.0.1"

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    tasks.withType<Test> {
        useJUnitPlatform()
    }
    tasks.withType<Wrapper> {
        gradleVersion = "8.5"
    }
}
