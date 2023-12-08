package ru.vi_tour.core_network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType

object AppJsonConfiguration {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
        encodeDefaults = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    val converterFactory = json.asConverterFactory("application/json".toMediaType())
}