package ru.otus.otuskotlin.m1l7.kt2java

import java.io.Serial

class MyClass(
    // прямой доступ к полю без геттеров и сеттеров
    @JvmField
    val a: String = "a-prop",

    // аннотация на геттер
    @get:Serial
    // аннотация на поле
    @field:Serial
    // сеттера нет
    val b: String = "b-prop",

    // аннотации на геттер и сеттер, поле недоступно
    @get:Serial
    @set:Serial
    var c: String = "c-prop",
)
