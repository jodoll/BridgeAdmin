/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import com.johannesdoll.bridgeadmin.huebridgeapi.pair.BridgePairCall
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpRequest
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

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

    @Test
    fun `When executing call, post to endpoint is made`() {

        val bridgeCall = BridgePairCall(client, "")
        runBlocking {
            bridgeCall.execute()
        }

        assertThat(recordedUrl).withFailMessage("Call not executed").isNotNull
        assertThat(recordedUrl.toString()).endsWith("/api")
    }

    @Test
    fun `Given a host url, when executing call, host is url prefix`() {
        val bridgeCall = BridgePairCall(client, "192.168.2.2")

        runBlocking {
            bridgeCall.execute()
        }

        assertThat(recordedUrl).isNotNull
        assertThat(recordedUrl.toString()).startsWith("http://192.168.2.2/")
    }

    @AfterEach
    fun tearDown() {
        recordedUrl = null
    }
}