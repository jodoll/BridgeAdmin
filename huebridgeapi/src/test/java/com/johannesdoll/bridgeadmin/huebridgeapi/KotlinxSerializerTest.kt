/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.jupiter.api.Test

class KotlinxSerializerTest {


    @Test
    fun `When parsing a nested object manually, no exception is thrown`() {
        Json.parse(
            OuterObject.serializer().list,
            "[{\"value\":{\"nestedValue\":101}}]"
        )
    }

    /**
     * This test asserts that when specifying a list serializer explicitly, no exception is thrown.
     *
     * This was previously a bug.
     *
     * See also:
     * [https://stackoverflow.com/questions/52971069/ktor-serialize-deserialize-json-with-list-as-root-in-multiplatform]
     */
    @Test
    fun `Given a list is added to the KotlinxSerializer explicitly, when deserializing a root level list, ktor does not throw an exception`() {
        val client = getMockEngine {
            respond(
                status = HttpStatusCode.OK,
                content = ByteReadChannel("[{\"value\":{\"nestedValue\":101}}]"),
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            )
        }

        assertThatCode {
            runBlocking {
                client.get<List<OuterObject>>("http://localhost:8080/nestedObject")
            }
        }.doesNotThrowAnyException()
    }

    private fun getMockEngine(callback: suspend MockEngineConfig.(HttpRequestData) -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer().apply {
                    register(OuterObject.serializer().list)
                }
            }
            engine {
                addHandler {
                    callback(it)
                }
            }
        }
    }

    @Serializable
    private data class OuterObject(
        val value: InnerObject
    )

    @Serializable
    private data class InnerObject(
        val nestedValue: Int
    )
}