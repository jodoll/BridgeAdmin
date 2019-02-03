/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi.pair

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.Url

class BridgePairCall(private val client: HttpClient, host: String) {

    companion object {
        private val BASE_URL = Url("http://localhost/api")
    }

    private val url = BASE_URL.copy(
        host = host
    )

    suspend fun execute() {
        client.post<Unit>(
            url
        )
    }

}
