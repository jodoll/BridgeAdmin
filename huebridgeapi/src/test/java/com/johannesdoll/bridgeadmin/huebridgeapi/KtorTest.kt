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
import kotlinx.coroutines.runBlocking
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
        runBlocking {
            val response = client.get<String>("${URL}/api/newdeveloper")
            System.out.println(response)
        }
    }

    @Test
    fun testPost() {
        runBlocking {
            val response = client.call("${URL}/api")
            System.out.println(response)
        }
    }

    @Test
    fun testPostWithBody() {
        runBlocking {
            val response = client.post<String> {
                url("${URL}/api")
                contentType(ContentType.Application.Json)
                body = AuthRequestBody("BridgeAdmin#ExploringTest")
            }
            System.out.println(response)
        }
    }

    @AfterEach
    fun tearDown() {
        client.close()
    }

    @Serializable
    data class AuthRequestBody(
        @SerialName("devicetype")
        val deviceType: String
    )
}