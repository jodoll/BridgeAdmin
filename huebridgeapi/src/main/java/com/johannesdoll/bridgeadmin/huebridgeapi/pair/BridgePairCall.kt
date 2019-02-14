/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi.pair

import com.johannesdoll.bridgeadmin.huebridgeapi.pair.HueErrorCodes.PAIRING_NEEDED
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.http.Url
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list

object HueErrorCodes {
    val PAIRING_NEEDED = 101
}

class BridgePairCall(private val client: HttpClient, host: String) {

    companion object {
        private val BASE_URL = Url("http://localhost/api")
    }

    val url = BASE_URL.copy(
        host = host
    )

    suspend fun execute(deviceType: String): PairCallResponse {
        val response = client.post<String> {
            url
            Request(deviceType)
        }

        val result = Json.parse(ErrorResponse.serializer().list, response)
        if (result[0].error.type == PAIRING_NEEDED) {
            return PairCallResponse.PairingNeeded
        }

        return PairCallResponse.PairingSuccessful()
    }

    @Serializable
    private data class Request(
        @SerialName("devicetype")
        val deviceType: String
    )

    @Serializable
    private data class ErrorResponse(
        val error: ErrorResponseDetails
    )

    @Serializable
    private data class ErrorResponseDetails(
        val type: Int,
        val address: String,
        val description: String
    )

}
