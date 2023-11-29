rootProject.name = "ok-marketplace-202310"

pluginManagement {
    val kotlinVersion: String by settings
    val kotestVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion
        id("io.kotest.multiplatform") version kotestVersion apply false
    }
}


//include("m1l1-quickstart")
//include("m1l2-basic")
//include("m1l3-oop")
//include("m1l4-dsl")
//include("m1l5-coroutines")
//include("m1l6-flows")
//include("m1l7-kmp")
include("m2l3-testing")

include("ok-marketplace-acceptance")
