package ru.otus.otuskotlin.marketplace.common

import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSessionRepo

data class MkplCorSettings(
    val wsSessions: IMkplWsSessionRepo = IMkplWsSessionRepo.NONE
) {
    companion object {
        val NONE = MkplCorSettings()
    }
}
