package ru.otus.m1l5.homework.hard.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Dictionary(
    val word: String,
    val meanings: List<Meaning>
)
