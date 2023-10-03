package ru.otus.otuskotlin.marketplace.mappers.v2

import ru.otus.otuskotlin.marketplace.api.v2.models.*
import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs

// Демонстрация форматной валидации в мапере
private sealed interface Result<T>
private data class Ok<T>(val value: T) : Result<T>
private data class Err<T>(val errors: List<MkplError>) : Result<T> {
    constructor(error: MkplError) : this(listOf(error))
}

private fun <T> Result<T>.getOrDefault(errors: MutableList<MkplError>, default: T): T = when (this) {
    is Ok<T> -> this.value
    is Err<T> -> {
        errors.addAll(this.errors)
        default
    }
}

@Suppress("unused")
private fun <T> Result<T>.getOrNull(errors: MutableList<MkplError>): T? = when (this) {
    is Ok<T> -> this.value
    is Err<T> -> {
        errors.addAll(this.errors)
        null
    }
}

private fun String?.transportToStubCaseValidated(): Result<MkplStubs> = when (this) {
    "success" -> Ok(MkplStubs.SUCCESS)
    "notFound" -> Ok(MkplStubs.NOT_FOUND)
    "badId" -> Ok(MkplStubs.BAD_ID)
    "badTitle" -> Ok(MkplStubs.BAD_TITLE)
    "badDescription" -> Ok(MkplStubs.BAD_DESCRIPTION)
    "badVisibility" -> Ok(MkplStubs.BAD_VISIBILITY)
    "cannotDelete" -> Ok(MkplStubs.CANNOT_DELETE)
    "badSearchString" -> Ok(MkplStubs.BAD_SEARCH_STRING)
    null -> Ok(MkplStubs.NONE)
    else -> Err(
        MkplError(
            code = "wrong-stub-case",
            group = "mapper-validation",
            field = "debug.stub",
            message = "Unsupported value for case \"$this\""
        )
    )
}

fun MkplContext.fromTransportValidated(request: AdCreateRequest) {
    command = MkplCommand.CREATE
    // Вся магия здесь!
    stubCase = request
        .debug
        ?.stub
        ?.value
        .transportToStubCaseValidated()
        .getOrDefault(errors, MkplStubs.NONE)
}
