package ru.vi_tour.core_network

import retrofit2.Response
import ru.vi_tour.core.AppError
import ru.vi_tour.core.Either
import java.net.ProtocolException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

abstract class BaseApi {
    suspend fun <E, R> doRequest(
        tag: String = "",
        request: suspend () -> Response<R>,
        mapper: suspend (R?) -> E?,
    ): Either<E> {
        return try {
            val response = request()
            if (response.isSuccessful) {
                Either.success(mapper(response.body()))
            } else {
                Either.error(AppError.Network())
            }
        } catch (e: SocketTimeoutException) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Network())
        } catch (e: UnknownHostException) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Network())
        } catch (e: SocketException) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Network())
        } catch (e: SSLPeerUnverifiedException) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Network())
        } catch (e: ProtocolException) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Network())
        } catch (e: Exception) {
            println("API_ERROR $tag ${e::class.simpleName} ${e.message}")
            Either.error(AppError.Unknown())
        }
    }
}