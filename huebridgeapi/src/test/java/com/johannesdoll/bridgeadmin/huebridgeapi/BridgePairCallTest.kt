/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpRequest
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class BridgePairCallTest {

    private var recordedUrl: Url? = null
    private var internalClient: HttpClient? = null
    private val client: HttpClient
        get() = internalClient!!

    @BeforeEach
    fun setup() {
        val engine = MockEngine {
            recordedUrl = url
            return@MockEngine MockHttpResponse(
                call,
                HttpStatusCode.OK
            )
        }
        internalClient = HttpClient(engine)
    }


    fun getMockEngine(callback: suspend MockHttpRequest.() -> MockHttpResponse): HttpClient {
        val engine = MockEngine(callback)
        return HttpClient(engine)
    }



    @AfterEach
    fun tearDown() {
        recordedUrl = null
    }
}