rootProject.name = "ok-marketplace-202310"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings
    val openapiVersion: String by settings
    val cwpGeneratorVersioin: String by settings
    val springframeworkBootVersion: String by settings
    val springDependencyManagementVersion: String by settings
    val ktorVersion: String by settings
    val bmuschkoVersion: String by settings
    val pluginShadow: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion apply false

        id("org.springframework.boot") version springframeworkBootVersion apply false
        id("io.spring.dependency-management") version springDependencyManagementVersion apply false
        kotlin("plugin.spring") version kotlinVersion apply false

        id("io.ktor.plugin") version ktorVersion apply false
        id("com.bmuschko.docker-remote-api") version bmuschkoVersion apply false

        id("io.kotest.multiplatform") version kotestVersion apply false
        id("org.openapi.generator") version openapiVersion apply false
        id("com.crowdproj.generator") version cwpGeneratorVersioin apply false
        id("com.github.johnrengelman.shadow") version pluginShadow apply false
    }
}


//include("m1l1-quickstart")
//include("m1l2-basic")
//include("m1l3-oop")
//include("m1l4-dsl")
//include("m1l5-coroutines")
//include("m1l6-flows")
//include("m1l7-kmp")
//include("m2l3-testing")
include("m4l4-konform")

include("ok-marketplace-acceptance")

include("ok-marketplace-api-v1-jackson")
include("ok-marketplace-api-v2-kmp")
include("ok-marketplace-api-log1")

include("ok-marketplace-common")
include("ok-marketplace-mappers-v1")
include("ok-marketplace-mappers-v2")
include("ok-marketplace-mappers-log1")

include("ok-marketplace-stubs")

include("ok-marketplace-biz")
include("ok-marketplace-lib-cor")

include("ok-marketplace-app-common")
include("ok-marketplace-app-spring")
include("ok-marketplace-app-ktor")
include("ok-marketplace-app-serverless")
include("ok-marketplace-app-rabbit")
include("ok-marketplace-app-kafka")

include("ok-marketplace-lib-logging-common")
include("ok-marketplace-lib-logging-kermit")
include("ok-marketplace-lib-logging-logback")
include("ok-marketplace-lib-logging-socket")
