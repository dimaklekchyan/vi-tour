package ru.vi_tour.core


sealed class AppError(val code: Int, private val message: String = "") {

    class Db(message: String = "") : AppError(db, message)
    class Network(message: String = "") : AppError(network, message)
    class NetworkServer(message: String = "") : AppError(network, message)
    class NetworkClient(message: String = "") : AppError(network, message)
    class Unknown(message: String = "") : AppError(unknown, message)

    companion object {
        private const val db = 10001
        private const val network = 10002
        private const val networkServer = 100021
        private const val networkClient = 100022
        private const val unknown = 100009

        fun error(code: Int, message: String = ""): AppError = when (code) {
            db -> Db(message)
            network -> Network(message)
            networkServer -> NetworkServer(message)
            networkClient -> NetworkClient(message)
            else -> Unknown(message)
        }
    }

    fun message() = message.ifEmpty {
        when (code) {
            db -> "Возникла ошибка на сервере"
            network -> "Ошибка сети. Проверьте подключение."
            networkServer -> "Ошибка на сервере."
            networkClient -> "Неверный запрос."
            else -> "Произошла неизвестная ошибка"
        }
    }
}

