package ru.otus.otuskotlin.markeplace.springapp

import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups

// TODO в наше приложение на спринге сейчас не прикручена авторизация
fun fakeMkplPrincipal() = MkplPrincipalModel(
    id = MkplUserId("user-1"),
    fname = "Ivan",
    mname = "Ivanovich",
    lname = "Ivanov",
    groups = setOf(MkplUserGroups.USER),
)
