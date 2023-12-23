package ru.otus.otuskotlin.marketplace.serverlessapp.api.v1

import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Request
import ru.otus.otuskotlin.marketplace.serverlessapp.api.model.Response
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.mappers.v1.*
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import yandex.cloud.sdk.functions.Context
import java.lang.RuntimeException

object CreateAdHandler : IV1HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("CreateAdHandler v1 start work")
        val request = req.decodeFromY<AdCreateRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportCreate().toResponse()
        }
    }
}

object ReadAdHandler : IV1HandleStrategy {
    override val path: String = "ad/read"
    override fun handle(req: Request, reqContext: Context): Response {
        println("ReadAdHandler v1 start work")
        val request = req.decodeFromY<AdReadRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportRead().toResponse()
        }
    }
}

object UpdateAdHandler : IV1HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {

        println("UpdateAdHandler v1 start work")
        val request = req.decodeFromY<AdUpdateRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportUpdate().toResponse()
        }

    }
}

object DeleteAdHandler : IV1HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("DeleteAdHandler v1 start work")
        val request = req.decodeFromY<AdDeleteRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            toTransportDelete().toResponse()
        }
    }
}

object SearchAdHandler : IV1HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("SearchAdHandler v1 start work")
        val request = req.decodeFromY<AdSearchRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adResponse = MkplAdStub.get()
            toTransportSearch().toResponse()
        }
    }
}

object OffersAdHandler : IV1HandleStrategy {
    override val path: String = "ad/create"
    override fun handle(req: Request, reqContext: Context): Response {
        println("OffersAdHandler v1 start work")
        val request = req.decodeFromY<AdOffersRequest>() ?: throw RuntimeException("Empty body")
        return withContext(reqContext) {
            fromTransport(request)
            adsResponse.add(MkplAdStub.get())
            toTransportOffers().toResponse()
        }
    }
}
