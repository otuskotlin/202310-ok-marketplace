package ru.otus.otuskotlin.marketplace.app.base

import io.ktor.server.auth.jwt.*
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.F_NAME_CLAIM
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.GROUPS_CLAIM
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.ID_CLAIM
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.L_NAME_CLAIM
import ru.otus.otuskotlin.marketplace.app.common.AuthConfig.Companion.M_NAME_CLAIM
import ru.otus.otuskotlin.marketplace.common.models.MkplUserId
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserGroups

fun JWTPrincipal?.toModel() = this?.run {
    MkplPrincipalModel(
        id = payload.getClaim(ID_CLAIM).asString()?.let { MkplUserId(it) } ?: MkplUserId.NONE,
        fname = payload.getClaim(F_NAME_CLAIM).asString() ?: "",
        mname = payload.getClaim(M_NAME_CLAIM).asString() ?: "",
        lname = payload.getClaim(L_NAME_CLAIM).asString() ?: "",
        groups = payload
            .getClaim(GROUPS_CLAIM)
            ?.asList(String::class.java)
            ?.mapNotNull {
                when(it) {
                    "USER" -> MkplUserGroups.USER
                    else -> null
                }
            }?.toSet() ?: emptySet()
    )
} ?: MkplPrincipalModel.NONE
