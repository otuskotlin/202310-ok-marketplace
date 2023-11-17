package ru.otus.otuskotlin.m1l7

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class UserService {
    actual fun serve(user: User): String = "Native Service for User $user"
}
