package ru.otus.otuskotlin.marketplace.app.rabbit.config

data class RabbitConfig(
    val host: String = HOST,
    val port: Int = PORT,
    val user: String = USER,
    val password: String = PASSWORD
) {
    constructor(vararg args: String) : this(
        host = args.arg("-h") ?: HOST,
        port = args.arg("-p")?.toInt() ?: PORT,
        user = args.arg("-u") ?: USER,
        password = args.arg("-pw") ?: PASSWORD,
    )

    companion object {
        const val HOST = "localhost"
        const val PORT = 5672
        const val USER = "guest"
        const val PASSWORD = "guest"
        private fun Array<out String>.arg(option: String) = indexOf(option)
            .takeIf { it != -1 }
            ?.let { this[it + 1] }
    }
}
