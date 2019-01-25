/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

class LoginUseCase(
    private val repo: LoginRepository,
    private val credentialsRepository: LoginCredentialsRepository,
    private val callback: LoginOutputPort
) : LoginRepository.Result {

    fun login(address: String) {
        repo.login(address, this)
    }

    override fun onSuccess(credentials: LoginCredentialsRepository.Credentials) {
        credentialsRepository.storeCredentials(credentials)
        callback.onLoginSuccessful()
    }

    override fun onChallengePresented() {
        callback.onChallengePresented()
    }

    override fun onError() {
        callback.onLoginFailed()
    }

    interface LoginOutputPort {
        fun onLoginSuccessful() {}
        fun onChallengePresented()
        fun onLoginFailed()
    }

}
