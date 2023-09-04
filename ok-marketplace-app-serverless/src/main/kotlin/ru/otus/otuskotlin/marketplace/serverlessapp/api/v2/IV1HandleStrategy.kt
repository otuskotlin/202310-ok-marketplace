package ru.otus.otuskotlin.marketplace.serverlessapp.api.v2

import ru.otus.otuskotlin.marketplace.serverlessapp.api.IHandleStrategy

sealed interface IV2HandleStrategy : IHandleStrategy {
    companion object {
        const val VERSION = "v2"
        private val strategies = listOf(
            CreateAdHandler,
            ReadAdHandler,
            UpdateAdHandler,
            DeleteAdHandler,
            SearchAdHandler,
            OffersAdHandler
        )
        val strategiesByDiscriminator = strategies.associateBy { it.path }
    }
}

