package ru.otus.otuskotlin.markeplace.springapp.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("sql")
data class SqlPropertiesEx(
    val url: String,
    val user: String,
    val password: String,
    val schema: String,
    val table: String?,
)
