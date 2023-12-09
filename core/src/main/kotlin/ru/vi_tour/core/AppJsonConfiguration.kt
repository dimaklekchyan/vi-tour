package ru.vi_tour.core

import kotlinx.serialization.json.Json

object AppJsonConfiguration {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
        encodeDefaults = true
    }
}