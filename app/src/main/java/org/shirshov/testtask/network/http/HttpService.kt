package org.shirshov.testtask.network.http

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import org.shirshov.testtask.network.utils.Path
import org.shirshov.testtask.util.LogUtil

class HttpService : BaseHttpService(Path.Server.API_ADDRESS)

open class BaseHttpService(val basePath: String) {

    companion object {
        private const val TIMEOUT = 10_000
    }

    val client = HttpClient(Android) {
        install(KtorSerializer)
        if (LogUtil.loggingEnabledHttp) install(KtorLogger)
        engine { connectTimeout = TIMEOUT; socketTimeout = TIMEOUT }
    }

    suspend inline fun <reified T> get(request: String, block: HttpRequestBuilder.() -> Unit = {}): T? {
        return client.get(basePath + request) {
            block()
        }
    }
}
