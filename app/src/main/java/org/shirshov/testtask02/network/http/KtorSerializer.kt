package org.shirshov.testtask02.network.http

import io.ktor.client.HttpClient
import io.ktor.client.call.TypeInfo
import io.ktor.client.features.HttpClientFeature
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.accept
import io.ktor.client.response.HttpResponseContainer
import io.ktor.client.response.HttpResponsePipeline
import io.ktor.client.utils.EmptyContent
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.content.OutgoingContent
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.util.AttributeKey
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.io.readRemaining
import kotlinx.io.core.Input
import kotlinx.io.core.readText
import org.shirshov.testtask02.network.utils.defaultObjectMapper

/**
 * Used instead of standard Ktor Jackson serializer to ensure usage of working Jackson serializer version
 *
 * https://github.com/FasterXML/jackson-module-kotlin/issues/176
 *
 * https://github.com/ktorio/ktor/issues/1238
 */
class KtorSerializer internal constructor() {

    private val acceptContentTypes: List<ContentType> = listOf(ContentType.Application.Json, ContentType.Text.JavaScript)

    private fun write(data: Any, contentType: ContentType): OutgoingContent = TextContent(defaultObjectMapper.writeValueAsString(data), contentType)

    private fun read(type: TypeInfo, body: Input): Any {
        return defaultObjectMapper.readValue(body.readText(), defaultObjectMapper.typeFactory.constructType(type.reifiedType))
    }

    companion object Feature : HttpClientFeature<Unit, KtorSerializer> {
        override val key: AttributeKey<KtorSerializer> = AttributeKey("Json")

        override fun prepare(block: Unit.() -> Unit): KtorSerializer {
            return KtorSerializer()
        }

        override fun install(feature: KtorSerializer, scope: HttpClient) {
            scope.requestPipeline.intercept(HttpRequestPipeline.Transform) { payload ->
                feature.acceptContentTypes.forEach { context.accept(it) }

                val contentType = context.contentType() ?: return@intercept
                if (feature.acceptContentTypes.none { contentType.match(it) })
                    return@intercept

                context.headers.remove(HttpHeaders.ContentType)

                val serializedContent = when (payload) {
                    is EmptyContent -> feature.write(Unit, contentType)
                    else -> feature.write(payload, contentType)
                }

                proceedWith(serializedContent)
            }

            scope.responsePipeline.intercept(HttpResponsePipeline.Transform) { (info, body) ->
                if (body !is ByteReadChannel) return@intercept

                if (feature.acceptContentTypes.none { context.response.contentType()?.match(it) == true })
                    return@intercept
                context.use {
                    proceedWith(HttpResponseContainer(info, feature.read(info, body.readRemaining())))
                }
            }
        }
    }
}
