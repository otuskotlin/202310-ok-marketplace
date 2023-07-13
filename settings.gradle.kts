rootProject.name = "ok-marketplace-202306"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false

    }
}

include("m1l1-quickstart")
include("m1l2-basic")
include("m1l3-oop")
include("m1l4-dsl")
