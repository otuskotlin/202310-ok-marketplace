package ru.otus.otuskotlin.marketplace.backend.repo.sql

import ru.otus.otuskotlin.marketplace.backend.repo.tests.*
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import kotlin.random.Random

val random = Random(System.currentTimeMillis())
class RepoAdSQLCreateTest : RepoAdCreateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "create-" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}

class RepoAdSQLDeleteTest : RepoAdDeleteTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "delete_" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLReadTest : RepoAdReadTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "read" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLSearchTest : RepoAdSearchTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "search" + random.nextInt(),
        initObjects
    )
}

class RepoAdSQLUpdateTest : RepoAdUpdateTest() {
    override val repo: IAdRepository = SqlTestCompanion.repoUnderTestContainer(
        "update" + random.nextInt(),
        initObjects,
        randomUuid = { lockNew.asString() },
    )
}
