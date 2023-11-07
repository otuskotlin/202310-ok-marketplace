@file:Suppress("PackageDirectoryMismatch")

package ru.otus.otuskotlin.marketplace.builder.kotlin

import ru.otus.otuskotlin.marketplace.builder.BreakfastBuilder

enum class Drink {
    WATER,
    COFFEE,
    TEA,
    NONE,
}

abstract class Meal {
    data class Breakfast(
        val eggs: Int,
        val bacon: Boolean,
        val drink: Drink,
        val title: String,
    ) : Meal()

    data class Dinner(
        val title: String,
    ) : Meal()
}

class KotlinLikeBuilder {
    var eggs = 0
    var bacon = false
    var drink = Drink.NONE
    var title = ""

    fun build() = Meal.Breakfast(eggs, bacon, drink, title)
}

fun breakfast(block: KotlinLikeBuilder.() -> Unit): Meal.Breakfast {
    val builder = KotlinLikeBuilder()
    builder.block()
    return builder.build()
}

// block: KotlinLikeBuilder.() -> Unit
fun KotlinLikeBuilder.block() {

}

fun main() {
    val breakfast = breakfast {
        title = "Cool breakfast"
        bacon = true
        eggs = 4
        drink = Drink.TEA
    }

    println(breakfast)
}
