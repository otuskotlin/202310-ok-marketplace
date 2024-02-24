package ru.otus.otuskotlin.markeplace.springapp

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import ru.otus.otuskotlin.marketplace.backend.repo.sql.RepoAdSQL
import kotlin.test.Test

@SpringBootTest
class ApplicationTests {
    @Suppress("unused")
    @MockBean
    private lateinit var repo: RepoAdSQL

    @Test
    fun contextLoads() {
    }
}
