package org.shirshov.testtask.network.http

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.features.observer.ResponseHandler
import io.ktor.client.features.observer.ResponseObserver
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.HttpSendPipeline
import io.ktor.client.response.HttpResponse
import io.ktor.client.response.HttpResponsePipeline
import io.ktor.http.*
import io.ktor.http.content.OutgoingContent
import io.ktor.util.AttributeKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.io.ByteChannel
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.close
import kotlinx.coroutines.io.readRemaining
import kotlinx.coroutines.launch
import kotlinx.io.charsets.Charset
import kotlinx.io.core.readText
import kotlinx.io.core.use
import org.shirshov.testtask.util.LogUtil

private const val MAX_AVAILABLE_LOG_LENGTH = 4000L // Ktor logger hangs when trying to read long response body. Total BS TODO try using pure okHttp

/**
 * Used to log http requests and responses done by Ktor http client
 */
class KtorLogger {

    // new line prefix to get beautiful log output
    private val prefix = "\n                                                                                  "

    private suspend fun logRequest(request: HttpRequestBuilder) {
        val content = request.body as OutgoingContent
        val headersLog = logHeaders(request.headers.entries(), content.headers)
        val bodyLog = logRequestBody(content)
        LogUtil.http("Http Request (${request.method.value}): ${Url(request.url)}$prefix $headersLog$prefix Content-Type: ${content.contentType} $prefix Body: $bodyLog")
    }

    private suspend fun logResponse(response: HttpResponse): Unit = response.use { httpResponse ->
        val responseBodyLog = logResponseBody(httpResponse.contentType(), httpResponse.content)
        LogUtil.http("Http Response: ${httpResponse.status}$prefix From (${httpResponse.call.request.method.value}): ${httpResponse.call.request.url}$prefix $responseBodyLog")
    }

    private fun logRequestException(context: HttpRequestBuilder, cause: Throwable) {
        LogUtil.http("Http Request ${Url(context.url)} failed with exception: $cause")
    }

    private fun logResponseException(context: HttpClientCall, cause: Throwable) {
        LogUtil.http("Http Response ${context.request.url} failed with exception: $cause")
    }

    private fun logHeaders(requestHeaders: Set<Map.Entry<String, List<String>>>, contentHeaders: Headers? = null): String {
        val commonHeadersBuilder = StringBuilder("Common headers$prefix ")
        requestHeaders.forEach { (key, values) ->
            commonHeadersBuilder.append("-> $key: ${values.joinToString("; ")}$prefix ")
        }
        return if (contentHeaders == null || contentHeaders.isEmpty()) {
            commonHeadersBuilder.toString()
        } else {
            val contentHeadersBuilder = java.lang.StringBuilder("Content headers$prefix ")
            contentHeaders.forEach { key, values ->
                contentHeadersBuilder.append("-> $key: ${values.joinToString("; ")}")
            }
            commonHeadersBuilder.toString() + contentHeadersBuilder.toString()
        }

    }

    private suspend fun logResponseBody(contentType: ContentType?, content: ByteReadChannel): String {
        val message = content.readText(contentType?.charset() ?: Charsets.UTF_8)
        return "Content-Type: $contentType$prefix Body: $message"
    }

    private suspend fun logRequestBody(content: OutgoingContent): String? {
        val charset = content.contentType?.charset() ?: Charsets.UTF_8
        return when (content) {
            is OutgoingContent.WriteChannelContent -> {
                val textChannel = ByteChannel()
                GlobalScope.launch(Dispatchers.Unconfined) {
                    content.writeTo(textChannel)
                    textChannel.close()
                }
                textChannel.readText(charset)
            }
            is OutgoingContent.ReadChannelContent -> {
                content.readFrom().readText(charset)
            }
            is OutgoingContent.ByteArrayContent -> kotlinx.io.core.String(content.bytes(), charset = charset)
            else -> null
        }
    }

    companion object : HttpClientFeature<Unit, KtorLogger> {
        override val key: AttributeKey<KtorLogger> = AttributeKey("ClientLogging")

        override fun prepare(block: Unit.() -> Unit): KtorLogger {
            return KtorLogger()
        }

        override fun install(feature: KtorLogger, scope: HttpClient) {
            scope.sendPipeline.intercept(HttpSendPipeline.Before) {
                try {
                    feature.logRequest(context)
                } catch (_: Throwable) {
                }
                try {
                    proceedWith(subject)
                } catch (cause: Throwable) {
                    feature.logRequestException(context, cause)
                    throw cause
                }
            }

            val observer: ResponseHandler = {
                try {
                    feature.logResponse(it)
                } catch (e: Throwable) {
                    LogUtil.e("Fail during logging", e)
                }
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Receive) {
                try {
                    proceedWith(subject)
                } catch (cause: Throwable) {
                    feature.logResponseException(context, cause)
                    throw cause
                }
            }


            ResponseObserver.install(ResponseObserver(observer), scope)
        }
    }

    private suspend inline fun ByteReadChannel.readText(charset: Charset): String = readRemaining(MAX_AVAILABLE_LOG_LENGTH).readText(charset = charset)
}
