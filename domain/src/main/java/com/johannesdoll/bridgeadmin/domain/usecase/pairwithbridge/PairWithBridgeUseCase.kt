/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.pairwithbridge

class PairWithBridgeUseCase(
    private val repo: PairWithBridgeRepository,
    private val credentialsRepository: PairWithBridgeCredentialsRepository,
    private val callback: OutputPort
) : PairWithBridgeRepository.Result {

    fun pair(address: String) {
        repo.pair(address, this)
    }

    override fun onSuccess(credentials: PairWithBridgeCredentialsRepository.Credentials) {
        credentialsRepository.storeCredentials(credentials)
        callback.onPairingSuccessful()
    }

    override fun onChallengePresented() {
        callback.onChallengePresented()
    }

    override fun onError() {
        callback.onPairingFailed()
    }

    interface OutputPort {
        fun onPairingSuccessful() {}
        fun onChallengePresented()
        fun onPairingFailed()
    }

}
