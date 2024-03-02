package ru.otus.otuskotlin.marketplace.backend.repository.gremlin

import com.benasher44.uuid.uuid4
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.exception.ResponseException
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource.traversal
import org.apache.tinkerpop.gremlin.process.traversal.TextP
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.apache.tinkerpop.gremlin.structure.Vertex
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_AD_TYPE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_LOCK
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_OWNER_ID
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_TITLE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.FIELD_TMP_RESULT
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdGremlinConst.RESULT_LOCK_FAILURE
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.addMkplAd
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.label
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.listMkplAd
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.mappers.toMkplAd
import ru.otus.otuskotlin.marketplace.common.helpers.asMkplError
import ru.otus.otuskotlin.marketplace.common.helpers.errorRepoConcurrency
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.repo.*
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.`__` as gr


class AdRepoGremlin(
    private val hosts: String,
    private val port: Int = 8182,
    private val enableSsl: Boolean = false,
    private val user: String = "root",
    private val pass: String = "",
    initObjects: Collection<MkplAd> = emptyList(),
    initRepo: ((GraphTraversalSource) -> Unit)? = null,
    val randomUuid: () -> String = { uuid4().toString() },
) : IAdRepository {

    val initializedObjects: List<MkplAd>

    private val cluster by lazy {
        Cluster.build().apply {
            addContactPoints(*hosts.split(Regex("\\s*,\\s*")).toTypedArray())
            port(port)
            credentials(user, pass)
            enableSsl(enableSsl)
        }.create()
    }
    private val g by lazy { traversal().withRemote(DriverRemoteConnection.using(cluster)) }

    init {
        if (initRepo != null) {
            initRepo(g)
        }
        initializedObjects = initObjects.map { save(it) }
    }

    private fun save(ad: MkplAd): MkplAd = g.addV(ad.label())
        .addMkplAd(ad)
        .listMkplAd()
        .next()
        ?.toMkplAd()
        ?: throw RuntimeException("Cannot initialize object $ad")

    override suspend fun createAd(rq: DbAdRequest): DbAdResponse {
        val key = randomUuid()
        val ad = rq.ad.copy(lock = MkplAdLock(randomUuid()))
        val dbRes: Map<String, Any?>? = try {
            g.addV(ad.label())
                .addMkplAd(ad)
                .listMkplAd()
                .next()
        } catch (e: Throwable) {
            return null.prepareResult(key = key, e = e)
        }
        return dbRes.prepareResult(key)
    }

    override suspend fun readAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val dbRes: Map<String, Any?>? = try {
            g.V(key).listMkplAd().next()
        } catch (e: Throwable) {
            return null.prepareResult(key = key, e = e)
        }
        return dbRes.prepareResult(key)
    }

    override suspend fun updateAd(rq: DbAdRequest): DbAdResponse {
        val key = rq.ad.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.ad.lock.takeIf { it != MkplAdLock.NONE } ?: return resultErrorEmptyLock
        val newLock = MkplAdLock(randomUuid())
        val newAd = rq.ad.copy(lock = newLock)
        val dbRes: Map<String, Any?>? = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Any>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a").addMkplAd(newAd).listMkplAd(),
                    gr.select<Vertex, Vertex>("a").listMkplAd(result = RESULT_LOCK_FAILURE)
                )
                .next()
        } catch (e: Throwable) {
            return null.prepareResult(key = key, e = e)
        }
        return dbRes.prepareResult(key, oldLock)
    }

    override suspend fun deleteAd(rq: DbAdIdRequest): DbAdResponse {
        val key = rq.id.takeIf { it != MkplAdId.NONE }?.asString() ?: return resultErrorEmptyId
        val oldLock = rq.lock.takeIf { it != MkplAdLock.NONE } ?: return resultErrorEmptyLock
        val dbRes: Map<String, Any?>? = try {
            g
                .V(key)
                .`as`("a")
                .choose(
                    gr.select<Vertex, Vertex>("a")
                        .values<String>(FIELD_LOCK)
                        .`is`(oldLock.asString()),
                    gr.select<Vertex, Vertex>("a")
                        .sideEffect(gr.drop<Vertex>())
                        .listMkplAd(),
                    gr.select<Vertex, Vertex>("a")
                        .listMkplAd(result = RESULT_LOCK_FAILURE)
                )
                .next()
        } catch (e: Throwable) {
            return null.prepareResult(key = key, e = e)
        }
        return dbRes.prepareResult(key, oldLock)
    }

    /**
     * Поиск объявлений по фильтру
     * Если в фильтре не установлен какой-либо из параметров - по нему фильтрация не идет
     */
    override suspend fun searchAd(rq: DbAdFilterRequest): DbAdsResponse {
        val result = try {
            g.V()
                .apply { rq.ownerId.takeIf { it != MkplUserId.NONE }?.also { has(FIELD_OWNER_ID, it.asString()) } }
                .apply { rq.dealSide.takeIf { it != MkplDealSide.NONE }?.also { has(FIELD_AD_TYPE, it.name) } }
                .apply {
                    rq.titleFilter.takeIf { it.isNotBlank() }?.also { has(FIELD_TITLE, TextP.containing(it)) }
                }
                .listMkplAd()
                .toList()
        } catch (e: Throwable) {
            return DbAdsResponse(
                isSuccess = false,
                data = null,
                errors = listOf(e.asMkplError())
            )
        }
        return DbAdsResponse(
            data = result.map { it.toMkplAd() },
            isSuccess = true
        )
    }

    companion object {
        private val resultErrorEmptyId = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )
        private val resultErrorEmptyLock = DbAdResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                MkplError(
                    field = "lock",
                    message = "Lock must be provided"
                )
            )
        )

        private fun Map<String, Any?>?.prepareResult(key: String, oldLock: MkplAdLock = MkplAdLock.NONE, e: Throwable? = null): DbAdResponse {
            val adResult = this?.toMkplAd()
            return when {
                e is ResponseException || e?.cause is ResponseException || adResult == null -> DbAdResponse(
                    isSuccess = false,
                    data = null,
                    errors = listOf(
                        MkplError(
                            code = "not-found",
                            field = "id",
                            message = "Not Found object with key $key",
                            exception = e
                        )
                    )
                )
                e != null -> DbAdResponse(
                    data = null,
                    isSuccess = false,
                    errors = listOf(e.asMkplError())
                )
                this?.get(FIELD_TMP_RESULT) == RESULT_LOCK_FAILURE -> DbAdResponse(
                    data = adResult,
                    isSuccess = false,
                    errors = listOf(
                        errorRepoConcurrency(
                            expectedLock = oldLock,
                            actualLock = adResult.lock,
                        ),
                    )
                )
                else -> DbAdResponse(
                    data = adResult,
                    isSuccess = true,
                )
            }
        }
    }
}
