/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.pairwithbridge


interface PairWithBridgeCredentialsRepository {
    fun storeCredentials(credentials: Credentials)

    interface Credentials {
    }
}
