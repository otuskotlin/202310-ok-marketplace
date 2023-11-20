package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups

fun MkplContext.addTestPrincipal(userId: MkplUserId = MkplUserId("321")) {
    principal = MkplPrincipalModel(
        id = userId,
        groups = setOf(
            MkplUserGroups.USER,
            MkplUserGroups.TEST,
        )
    )
}
