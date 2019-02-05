/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi.pair

import com.johannesdoll.bridgeadmin.huebridgeapi.relaxedMockk
import io.ktor.client.HttpClient
import io.mockk.clearMocks
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class BridgePairCallUrlTest {

    private val client: HttpClient = relaxedMockk()

    @Test
    fun `When creating call, url ends with right suffix`() {
        val bridgeCall = BridgePairCall(client, "")

        Assertions.assertThat(bridgeCall.url.toString()).endsWith("/api")
    }

    @Test
    fun `Given a host, when creating call, host is url prefix`() {
        val bridgeCall = BridgePairCall(client, "192.168.2.2")

        Assertions.assertThat(bridgeCall.url.toString()).startsWith("http://192.168.2.2/")
    }

    @AfterEach
    fun tearDown() {
        clearMocks(client)
    }
}