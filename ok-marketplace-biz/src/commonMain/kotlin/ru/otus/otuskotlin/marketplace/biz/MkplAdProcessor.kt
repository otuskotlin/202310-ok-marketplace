package ru.otus.otuskotlin.marketplace.biz

import ru.otus.otuskotlin.marketplace.common.MkplContext
import ru.otus.otuskotlin.marketplace.common.models.MkplCommand
import ru.otus.otuskotlin.marketplace.common.models.MkplDealSide
import ru.otus.otuskotlin.marketplace.common.models.MkplWorkMode
import ru.otus.otuskotlin.marketplace.stubs.MkplAdStub

@Suppress("RedundantSuspendModifier")
class MkplAdProcessor {
    suspend fun exec(ctx: MkplContext) {
        // TODO: Rewrite temporary stub solution with BIZ
        require(ctx.workMode == MkplWorkMode.STUB) {
            "Currently working only in STUB mode."
        }

        when (ctx.command) {
            MkplCommand.SEARCH -> {
                ctx.adsResponse.addAll(MkplAdStub.prepareSearchList("Болт", MkplDealSide.DEMAND))
            }
            MkplCommand.OFFERS -> {
                ctx.adsResponse.addAll(MkplAdStub.prepareOffersList("Болт", MkplDealSide.SUPPLY))
            }
            else -> {
                ctx.adResponse = MkplAdStub.get()
            }
        }
    }
}
