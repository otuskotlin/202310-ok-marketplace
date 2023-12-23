package ru.otus.otuskotlin.marketplace.serverlessapp.api.v1

import ru.otus.otuskotlin.marketplace.serverlessapp.api.IHandleStrategy

sealed interface IV1HandleStrategy : IHandleStrategy {
    companion object {
        const val VERSION = "v1"
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

