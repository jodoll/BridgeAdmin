/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockHttpRequest
import io.ktor.client.engine.mock.MockHttpResponse
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.io.ByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import org.assertj.core.api.Assertions.assertThatThrownBy
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
     * This test asserts that a bug is still present where ktor is unable to deserialize root level lists, even when
     * the serializer is provided explicitly. If this test fails the bug has either been fixed or a different exception
     * is thrown.
     *
     * See also:
     * [https://stackoverflow.com/questions/52971069/ktor-serialize-deserialize-json-with-list-as-root-in-multiplatform]
     */
    @Test
    fun `When deserializing a root level list, ktor throws an exception`() {
        val client = getMockEngine {
            MockHttpResponse(
                call,
                HttpStatusCode.OK,
                ByteReadChannel("[{\"value\":{\"nestedValue\":101}}]"),
                headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        assertThatThrownBy {
            runBlocking {
                client.get<List<OuterObject>>("http://localhost:8080/nestedObject")
            }
        }.extracting(Throwable::cause)
            .isInstanceOf(SerializationException::class.java)
            .withFailMessage("Can't locate argument-less serializer for class kotlin.collections.List. For generic classes, such as lists, please provide serializer explicitly.")

    }

    private fun getMockEngine(callback: suspend MockHttpRequest.() -> MockHttpResponse): HttpClient {
        val engine = MockEngine(callback)
        return HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer().apply {
                    registerList(OuterObject.serializer())
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