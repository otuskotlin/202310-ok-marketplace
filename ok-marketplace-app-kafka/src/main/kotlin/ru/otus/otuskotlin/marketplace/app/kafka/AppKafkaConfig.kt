package ru.otus.otuskotlin.marketplace.app.kafka

import ru.otus.otuskotlin.marketplace.app.common.AuthConfig
import ru.otus.otuskotlin.marketplace.app.common.IMkplAppSettings
import ru.otus.otuskotlin.marketplace.biz.MkplAdProcessor
import ru.otus.otuskotlin.marketplace.common.MkplCorSettings

class AppKafkaConfig(
    val kafkaHosts: List<String> = KAFKA_HOSTS,
    val kafkaGroupId: String = KAFKA_GROUP_ID,
    val kafkaTopicInV1: String = KAFKA_TOPIC_IN_V1,
    val kafkaTopicOutV1: String = KAFKA_TOPIC_OUT_V1,
    val kafkaTopicInV2: String = KAFKA_TOPIC_IN_V2,
    val kafkaTopicOutV2: String = KAFKA_TOPIC_OUT_V2,
    override val processor: MkplAdProcessor = MkplAdProcessor(),
    override val corSettings: MkplCorSettings = MkplCorSettings(),
    override val auth: AuthConfig = AuthConfig.TEST,
): IMkplAppSettings {
    companion object {
        const val KAFKA_HOST_VAR = "KAFKA_HOSTS"
        const val KAFKA_TOPIC_IN_V1_VAR = "KAFKA_TOPIC_IN_V1"
        const val KAFKA_TOPIC_OUT_V1_VAR = "KAFKA_TOPIC_OUT_V1"
        const val KAFKA_TOPIC_IN_V2_VAR = "KAFKA_TOPIC_IN_V2"
        const val KAFKA_TOPIC_OUT_V2_VAR = "KAFKA_TOPIC_OUT_V2"
        const val KAFKA_GROUP_ID_VAR = "KAFKA_GROUP_ID"

        val KAFKA_HOSTS by lazy { (System.getenv(KAFKA_HOST_VAR) ?: "").split("\\s*[,;]\\s*") }
        val KAFKA_GROUP_ID by lazy { System.getenv(KAFKA_GROUP_ID_VAR) ?: "marketplace" }
        val KAFKA_TOPIC_IN_V1 by lazy { System.getenv(KAFKA_TOPIC_IN_V1_VAR) ?: "marketplace-in-v1" }
        val KAFKA_TOPIC_OUT_V1 by lazy { System.getenv(KAFKA_TOPIC_OUT_V1_VAR) ?: "marketplace-out-v1" }
        val KAFKA_TOPIC_IN_V2 by lazy { System.getenv(KAFKA_TOPIC_IN_V2_VAR) ?: "marketplace-in-v2" }
        val KAFKA_TOPIC_OUT_V2 by lazy { System.getenv(KAFKA_TOPIC_OUT_V2_VAR) ?: "marketplace-out-v2" }
    }
}
