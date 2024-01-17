package ru.otus.otuskotlin.markeplace.springapp.models

import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

data class MkplAppSettings(
    override val processor: MkplAdProcessor,
    override val corSettings: MkplCorSettings,
): IMkplAppSettings
