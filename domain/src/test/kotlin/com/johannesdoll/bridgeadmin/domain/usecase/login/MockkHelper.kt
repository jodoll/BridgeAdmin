/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

import io.mockk.mockk

inline fun <reified T : Any> relaxedMockk(): T {
    return mockk(relaxed = true)
}