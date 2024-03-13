package ru.otus.otuskotlin.marketplace.app.ktor.helpers

import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.backend.repository.inmemory.AdRepoStub
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import ru.otus.otuskotlin.marketplace.repo.inmemory.AdRepoInMemory

fun testSettings(repo: IAdRepository? = null) = MkplAppSettings(
    corSettings = MkplCorSettings(
        repoStub = AdRepoStub(),
        repoTest = repo ?: AdRepoInMemory(),
        repoProd = repo ?: AdRepoInMemory(),
    )
)
