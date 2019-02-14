/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.list
import org.junit.jupiter.api.Test

class KotlinxSerializerTest {

    @Test
    fun `When parsing a nested object manually, no exception is thrown`() {
        Json.parse(
            OuterObject.serializer().list,
            "[{\"value\":{\"nestedValue\":101}}]"
        )
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