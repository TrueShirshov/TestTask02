package org.shirshov.testtask02.network.repository

import io.ktor.client.features.ClientRequestException
import io.ktor.client.response.HttpResponse
import kotlinx.coroutines.*
import org.shirshov.testtask02.network.http.*

abstract class BaseHttpRepository {

    suspend fun <T> request(block: suspend CoroutineScope.() -> T?): UnprocessedResponse<T> {
        return withContext((CoroutineScope(Dispatchers.IO) + Job()).coroutineContext) {
            try {
                UnprocessedResponse(Response(block(), null))
            } catch (e: Exception) {
                var mappedException = e
                if (e is ClientRequestException) {
                    mappedException = mapErrorCode(e.response.status.value, e)
                }
                UnprocessedResponse(Response(null, mappedException))
            }
        }
    }

    fun processStatus(httpResponse: HttpResponse?) =
        when (val code = httpResponse?.status?.value) {
            in 200..300 -> true
            null -> null
            else -> throw mapErrorCode(code)
        }

    private fun mapErrorCode(code: Int, cause: Exception? = null) = when (code) {
        400 -> BadRequestException()
        401 -> UnauthorizedException()
        404 -> NotFoundException()
        500 -> InternalServerErrorException()
        else -> Exception("Http response status code is $code").also { ex -> cause?.let { ex.initCause(it) } }
    }
}