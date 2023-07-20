rootProject.name = "ok-marketplace-202310"

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
include("m1l5-coroutines")
include("m1l6-flows-and-channels")
