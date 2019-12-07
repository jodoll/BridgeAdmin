/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi.pair

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class BridgePairCallTest {

    private fun getMockEngine(callback: suspend MockEngineConfig.(HttpRequestData) -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            engine {
                addHandler {
                    callback(it)
                }
            }
        }
    }

    @Test
    fun `Given a device type, when executing call and pairing is needed, response is pairing press link`() {
        val client = getMockEngine {
            respond(
                ByteReadChannel("[{\"error\":{\"type\":101,\"address\":\"\",\"description\":\"link button not pressed\"}}]"),
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val bridgeCall = BridgePairCall(client, "192.168.2.4")

        val response = runBlocking {
            bridgeCall.execute("nexus#User1")
        }

        assertThat(response is PairCallResponse.PairingNeeded).isTrue()
    }

    @Disabled("correct response is needed")
    @Test
    fun `Given a device type, when executing call and pairing is successful, response contains type`() {
        val client = getMockEngine {
            respond(
                ByteReadChannel("[{\"error\":{\"type\":101,\"address\":\"\",\"description\":\"link button not pressed\"}}]"),
                HttpStatusCode.OK,
                headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val bridgeCall = BridgePairCall(client, "192.168.2.4")

        val response = runBlocking {
            bridgeCall.execute("nexus#User1")
        }

        assertThat(response is PairCallResponse.PairingNeeded).isTrue()
        //assertThat((response as PairCallResponse.PairingNeeded).deviceType).isEqualTo("nexus#User1")
    }

}