package ru.otus.otuskotlin.marketplace.common.repo

interface IAdRepository {
    suspend fun createAd(rq: DbAdRequest): DbAdResponse
    suspend fun readAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun updateAd(rq: DbAdRequest): DbAdResponse
    suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse
    suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse
    companion object {
        val NONE = object : IAdRepository {
            override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
