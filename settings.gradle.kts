rootProject.name = "ok-marketplace-202310"

pluginManagement {
    plugins {
        val kotlinVersion: String by settings

        kotlin("jvm") version kotlinVersion apply false

    }
}

include("m1l1-quickstart")
