package ru.otus.otuskotlin.marketplace.blackbox.test

import io.kotest.core.spec.style.FunSpec
import ru.otus.otuskotlin.marketplace.blackbox.fixture.client.Client
import ru.otus.otuskotlin.marketplace.blackbox.test.action.v1.createAd

fun FunSpec.testApiV1(client: Client) {
    context("v1") {
        test("Create Ad ok") {
            client.createAd()
        }
    }
}