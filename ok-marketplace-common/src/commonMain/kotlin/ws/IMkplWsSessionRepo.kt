package ru.otus.otuskotlin.marketplace.common.ws

interface IMkplWsSessionRepo {
    fun add(session: IMkplWsSession)
    fun clearAll()
    fun remove(session: IMkplWsSession)
    suspend fun <K> sendAll(obj: K)
}
