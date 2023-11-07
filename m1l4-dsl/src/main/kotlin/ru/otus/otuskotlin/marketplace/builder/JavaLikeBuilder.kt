package ru.otus.otuskotlin.marketplace.builder

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

class BreakfastBuilder {
    var eggs = 0
    var bacon = false
    var drink = Drink.NONE
    var title = ""

    fun withEggs(eggs: Int): BreakfastBuilder {
        this.eggs = eggs
        return this
    }

    fun withBacon(bacon: Boolean): BreakfastBuilder {
        this.bacon = bacon
        return this
    }

    fun withDrink(drink: Drink): BreakfastBuilder {
        this.drink = drink
        return this
    }

    fun withTitle(title: String): BreakfastBuilder {
        this.title = title
        return this
    }

    fun build() = Meal.Breakfast(eggs, bacon, drink, title)
}

fun main() {
    val builder = BreakfastBuilder()
        .withEggs(12)
        .withBacon(true)
        .withDrink(Drink.COFFEE)
        .withTitle("Default breakfast")

    // do some code
    builder.withEggs(13)

    val breakfast = builder.build()
    println(breakfast)

//
//    val breakfastNew = Meal.Breakfast(
//        eggs = 12,
//        bacon = true,
//        drink = Drink.TEA,
//        "New breakfast"
//    )
//
//    breakfastNew.copy(eggs = 13)
}
