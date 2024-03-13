package ru.otus.otuskotlin.marketplace.common

import kotlinx.datetime.Instant
import ru.otus.otuskotlin.marketplace.common.models.*
import ru.otus.otuskotlin.marketplace.common.permissions.MkplPrincipalModel
import ru.otus.otuskotlin.marketplace.common.permissions.MkplUserPermissions
import ru.otus.otuskotlin.marketplace.common.repo.IAdRepository
import ru.otus.otuskotlin.marketplace.common.stubs.MkplStubs
import ru.otus.otuskotlin.marketplace.common.ws.IMkplWsSession

data class MkplContext(
    var command: MkplCommand = MkplCommand.NONE,
    var state: MkplState = MkplState.NONE,
    val errors: MutableList<MkplError> = mutableListOf(),
    var corSettings: MkplCorSettings = MkplCorSettings(),

    var workMode: MkplWorkMode = MkplWorkMode.PROD,
    var stubCase: MkplStubs = MkplStubs.NONE,

    var adRepo: IAdRepository = IAdRepository.NONE,
    var adRepoRead: MkplAd = MkplAd(), // То, что прочитали из репозитория
    var adRepoPrepare: MkplAd = MkplAd(), // То, что готовим для сохранения в БД
    var adRepoDone: MkplAd = MkplAd(),  // Результат, полученный из БД
    var adsRepoDone: MutableList<MkplAd> = mutableListOf(),

    var principal: MkplPrincipalModel = MkplPrincipalModel.NONE,
    // Набор пермишинов, которые соответствуют группам, полученным из JWT в запросе
    val permissionsChain: MutableSet<MkplUserPermissions> = mutableSetOf(),
    // Результат вычисления доступа (да/нет)
    var permitted: Boolean = false,

    var requestId: MkplRequestId = MkplRequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var adRequest: MkplAd = MkplAd(),
    var adFilterRequest: MkplAdFilter = MkplAdFilter(),

    var adValidating: MkplAd = MkplAd(),
    var adFilterValidating: MkplAdFilter = MkplAdFilter(),

    var adValidated: MkplAd = MkplAd(),
    var adFilterValidated: MkplAdFilter = MkplAdFilter(),

    var adResponse: MkplAd = MkplAd(),
    var adsResponse: MutableList<MkplAd> = mutableListOf(),

    //ws
    var session: IMkplWsSession = IMkplWsSession.NONE
)
