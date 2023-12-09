package ru.vi_tour.core


sealed class AppError(val code: Int, private val message: String = "") {

    class Db(message: String = "") : AppError(db, message)
    class Network(message: String = "") : AppError(network, message)
    class NetworkServer(message: String = "") : AppError(network, message)
    class NetworkClient(message: String = "") : AppError(network, message)
    class Unknown(message: String = "") : AppError(unknown, message)
    class FileDoesntExist(message: String = "") : AppError(unknown, message)

    companion object {
        private const val db = 1000
        private const val network = 2000
        private const val networkServer = 2001
        private const val networkClient = 2002
        private const val unknown = 9999
        private const val fileDoesntExist = 3000

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
            fileDoesntExist -> "Файл не существует"
            else -> "Произошла неизвестная ошибка"
        }
    }
}

