package ru.otus.otuskotlin.m1l7

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue

class UserServiceTest {

    @Test
    fun firstKmp() {
        val user = User("1", "Ivan", 24)
        val service = UserService()
        val res = service.serve(user)
        assertContains(res, "Service for User")
        assertTrue {
            res.contains("JS")
                    || res.contains("JVM")
                    || res.contains("Native")
        }
    }
}
