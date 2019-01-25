/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login


interface LoginCredentialsRepository {
    fun storeCredentials(credentials: Credentials)

    interface Credentials {
    }
}
