package ru.otus.otuskotlin.marketplace.app.repo

import com.benasher44.uuid.uuid4
import org.testcontainers.containers.PostgreSQLContainer
import ru.otus.otuskotlin.marketplace.backend.repo.sql.RepoAdSQL
import ru.otus.otuskotlin.marketplace.backend.repo.sql.SqlProperties
import ru.otus.otuskotlin.marketplace.common.models.MkplAd
import java.time.Duration

class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.2")

object SqlTestCompanion {
    private const val USER = "postgres"
    private const val PASS = "marketplace-pass"
    private const val SCHEMA = "marketplace"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
        }
    }

    fun start() {
        container.start()
    }

    fun stop() {
        container.stop()
    }

    private val url: String by lazy { container.jdbcUrl }

    fun repoUnderTestContainer(
        test: String,
        initObjects: Collection<MkplAd> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() },
    ): RepoAdSQL {
        return RepoAdSQL(
            properties = SqlProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
                table = "ad-$test",
            ),
            initObjects = initObjects,
            randomUuid = randomUuid
        )
    }
}
