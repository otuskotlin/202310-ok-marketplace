package ru.otus.otuskotlin.marketplace.blackbox._draft

import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseSeverityLevel
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class DraftTest : FunSpec({
    test("my first test") {
        1 + 2 shouldBe 3
    }
    test("other test") {
        2 * 2 shouldNotBe 3
    }
})
