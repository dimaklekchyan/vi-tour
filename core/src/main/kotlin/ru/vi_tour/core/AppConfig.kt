package ru.vi_tour.core

object AppConfig {
    object Test {
        internal const val useDebugUrl: Boolean = false
    }

    object Api {
        private const val prodUrl = "https://vi-tour.ru/cabinet/api/v1/video/add/"
        private const val debugUrl = ""

        val baseUrl = if(Test.useDebugUrl) debugUrl else prodUrl
    }
}