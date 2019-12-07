/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class KtorTest {

    private val URL = "http://192.168.2.168"
    private lateinit var client: HttpClient

    @BeforeEach
    fun setup() {
        client = HttpClient(CIO) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
        }
    }

    @Test
    fun testGet() {
        runBlockingWithTimeout {
            val response = client.get<String>("${Companion.URL}/api/newdeveloper")
            println(response)
        }
    }

    @Test
    fun testPost() {
        runBlockingWithTimeout {
            val response = client.call("${Companion.URL}/api")
            println(response)
        }
    }

    @Test
    fun testPostWithBody() {
        runBlockingWithTimeout {
            val response = client.post<String> {
                url("${Companion.URL}/api")
                contentType(ContentType.Application.Json)
                body = AuthRequestBody("BridgeAdmin#ExploringTest")
            }
            println(response)
        }
    }

    @AfterEach
    fun tearDown() {
        client.close()
    }

    @Serializable
    private data class AuthRequestBody(
        @SerialName("devicetype")
        val deviceType: String
    )

    private fun <T> runBlockingWithTimeout(
        timeout: Long = 3000,
        suspendBlock: suspend CoroutineScope.() -> T
    ) {
        runBlocking {
            withTimeout(timeout, suspendBlock)
        }
    }
}