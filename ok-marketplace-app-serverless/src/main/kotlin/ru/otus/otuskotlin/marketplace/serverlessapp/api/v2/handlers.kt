package ru.otus.otuskotlin.marketplace.serverlessapp.api.v2

import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Request
import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Response
import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.mappers.v2.fromTransport
import ru.otus.otuskotlin.marketplace.mappers.v2.toTransportCreate
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import yandex.cloud.sdk.functions.Context
import java.lang.RuntimeException

object CreateAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("CreateAdHandler v2 start work")
        val request = req.decodeFromY<AdCreateRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object ReadAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("ReadAdHandler v2 start work")
        val request = req.decodeFromY<AdReadRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object UpdateAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("UpdateAdHandler v2 start work")
        val request = req.decodeFromY<AdUpdateRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object DeleteAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("DeleteAdHandler v2 start work")
        val request = req.decodeFromY<AdDeleteRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            toTransportCreate().toResponse()
        }
    }
}

object SearchAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("SearchAdHandler v2 start work")
        val request = req.decodeFromY<AdSearchRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object OffersAdHandler : IV2HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("OffersAdHandler v2 start work")
        val request = req.decodeFromY<AdOffersRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adsResponse.add(MkplAdStub.get())
            toTransportCreate().toResponse()
        }
    }
}
