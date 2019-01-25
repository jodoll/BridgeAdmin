/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.pairwithbridge

interface PairWithBridgeRepository {

    fun pair(
        address: String,
        deviceName: String,
        callback: Result
    )

    interface Result {
        fun onSuccess(credentials: PairWithBridgeCredentialsRepository.Credentials)
        fun onChallengePresented()
        fun onError()
    }
}
