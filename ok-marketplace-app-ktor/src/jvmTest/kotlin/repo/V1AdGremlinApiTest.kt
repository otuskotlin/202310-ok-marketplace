package ru.otus.otuskotlin.marketplace.app.ktor.repo

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.testing.*
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.images.RemoteDockerImage
import org.testcontainers.utility.DockerImageName
import ru.otus.otuskotlin.marketplace.api.v1.models.*
import ru.otus.otuskotlin.marketplace.app.ktor.MkplAppSettings
import ru.otus.otuskotlin.marketplace.app.ktor.moduleJvm
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.ARCADEDB_VERSION
import ru.otus.otuskotlin.marketplace.backend.repository.gremlin.AdRepoGremlin
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub
import java.time.Duration
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class V1AdGremlinApiTest {

    @Test
    fun create() = testApplication {
//        val repo = repoUnderTestContainer(test = "create", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val createAd = AdCreateObject(
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
        )

        val response = client.post("/v1/ad/create") {
            val requestObj = AdCreateRequest(
                requestId = "12345",
                ad = createAd,
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdCreateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(createAd.title, responseObj.ad?.title)
        assertEquals(createAd.description, responseObj.ad?.description)
        assertEquals(createAd.adType, responseObj.ad?.adType)
        assertEquals(createAd.visibility, responseObj.ad?.visibility)
        assertEquals(uuidNew, responseObj.ad?.lock)
    }

    @Test
    fun read() = testApplication {
//        val repo = repoUnderTestContainer(test = "read", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/read") {
            val requestObj = AdReadRequest(
                requestId = "12345",
                ad = AdReadObject(initAd.id.asString()),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdReadResponse>()
        assertEquals(200, response.status.value)
        assertEquals(initAd.id.asString(), responseObj.ad?.id)
    }

    @Test
    fun update() = testApplication {
//        val repo = repoUnderTestContainer(test = "update", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val adUpdate = AdUpdateObject(
            id = initAd.id.asString(),
            title = "Болт",
            description = "КРУТЕЙШИЙ",
            adType = DealSide.DEMAND,
            visibility = AdVisibility.PUBLIC,
            lock = initAd.lock.asString(),
        )

        val response = client.post("/v1/ad/update") {
            val requestObj = AdUpdateRequest(
                requestId = "12345",
                ad = adUpdate,
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdUpdateResponse>()
        assertEquals(200, response.status.value)
        assertEquals(adUpdate.id, responseObj.ad?.id)
        assertEquals(adUpdate.title, responseObj.ad?.title)
        assertEquals(adUpdate.description, responseObj.ad?.description)
        assertEquals(adUpdate.adType, responseObj.ad?.adType)
        assertEquals(adUpdate.visibility, responseObj.ad?.visibility)
    }

    @Test
    fun delete() = testApplication {
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/delete") {
            val requestObj = AdDeleteRequest(
                requestId = "12345",
                ad = AdDeleteObject(
                    id = initAdDelete.id.asString(),
                    lock = initAdDelete.lock.asString(),
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdDeleteResponse>()
        assertEquals(200, response.status.value)
        assertEquals(initAdDelete.id.asString(), responseObj.ad?.id)
    }

    @Test
    fun search() = testApplication {
//        val repo = repoUnderTestContainer(test = "search", initObjects = listOf(initAd), randomUuid = { uuidNew })
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/search") {
            val requestObj = AdSearchRequest(
                requestId = "12345",
                adFilter = AdSearchFilter(),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdSearchResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertContains(responseObj.ads?.map { it.id }?: emptyList(), initAd.id.asString())
    }

    @Test
    fun offers() = testApplication {
        application {
            moduleJvm(MkplAppSettings(corSettings = MkplCorSettings(repoTest = repo)))
        }
        val client = myClient()

        val response = client.post("/v1/ad/offers") {
            val requestObj = AdOffersRequest(
                requestId = "12345",
                ad = AdReadObject(
                    id = initAd.id.asString(),
                ),
                debug = AdDebug(
                    mode = AdRequestDebugMode.TEST,
                )
            )
            contentType(ContentType.Application.Json)
            setBody(requestObj)
        }
        val responseObj = response.body<AdOffersResponse>()
        assertEquals(200, response.status.value)
        assertNotEquals(0, responseObj.ads?.size)
        assertEquals(initAdSupply.id.asString(), responseObj.ads?.first()?.id)
    }

    private fun ApplicationTestBuilder.myClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }

    companion object {
        @BeforeClass
        @JvmStatic
        fun tearUp() {
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            container.stop()
        }

        private const val USER = "root"
        private const val PASS = "marketplace-pass"

        private val container by lazy {
            GenericContainer(RemoteDockerImage(DockerImageName.parse("arcadedata/arcadedb:${ARCADEDB_VERSION}"))).apply {
                withExposedPorts(2480, 2424, 8182)
                withEnv(
                    "JAVA_OPTS",
                    "-Darcadedb.server.rootPassword=$PASS " +
                            "-Darcadedb.server.plugins=GremlinServer:com.arcadedb.server.gremlin.GremlinServerPlugin"
                )
                waitingFor(Wait.forLogMessage(".*ArcadeDB Server started.*\\n", 1))
                withStartupTimeout(Duration.ofMinutes(5))
                start()
            }
        }

        private val uuidOld = "10000000-0000-0000-0000-000000000001"
        private val uuidNew = "10000000-0000-0000-0000-000000000002"

        private val initObjects = listOf(
            MkplAdStub.prepareResult {
                id = MkplAdId.NONE
                title = "abc"
                description = "abc"
                adType = MkplDealSide.DEMAND
                visibility = MkplVisibility.VISIBLE_PUBLIC
                lock = MkplAdLock(uuidOld)
            },
            MkplAdStub.prepareResult {
                id = MkplAdId.NONE
                title = "delete"
                description = "delete"
                adType = MkplDealSide.DEMAND
                visibility = MkplVisibility.VISIBLE_PUBLIC
                lock = MkplAdLock(uuidOld)
            },
            MkplAdStub.prepareResult {
                id = MkplAdId.NONE
                title = "abc"
                description = "abc"
                adType = MkplDealSide.SUPPLY
                visibility = MkplVisibility.VISIBLE_PUBLIC
            }
        )
        private val repo by lazy {
            AdRepoGremlin(
                hosts = container.host,
                user = USER,
                pass = PASS,
                port = container.getMappedPort(8182),
                initObjects = initObjects,
                randomUuid = { uuidNew }
            )
        }

        private val initAd = repo.initializedObjects.first()
        private val initAdDelete = repo.initializedObjects[1]
        private val initAdSupply = repo.initializedObjects.last()
    }
}
