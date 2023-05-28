package ru.otus.otuskotlin.marketplace.blackbox._draft

import io.kotest.core.annotation.Ignored
import io.kotest.core.spec.style.FunSpec
import io.kotest.core.test.TestCaseSeverityLevel
import io.kotest.matchers.shouldBe

@Ignored
open class DraftTestBase(
    prop: String,
) : FunSpec({
    test("$prop: my first test").config(severity = TestCaseSeverityLevel.MINOR) {
        1 + 2 shouldBe 4
    }
})
