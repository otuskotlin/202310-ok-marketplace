package ru.otus.otuskotlin.m1l7

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserAsyncService actual constructor() {
    actual suspend fun serve(user: User): Pair<String, User> = "JS" to user
}
