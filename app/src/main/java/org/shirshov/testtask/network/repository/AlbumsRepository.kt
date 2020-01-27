package org.shirshov.testtask.network.repository

import io.ktor.client.response.HttpResponse
import org.shirshov.testtask.network.http.HttpService

val httpService = HttpService("http://itunes.apple.com")

class AlbumsRepository : BaseHttpRepository() {

    suspend fun test() =
        request {
            httpService.get<HttpResponse>("/search?term=mezmerize&limit=50")//&media=music
        }

}