/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

class LoginUseCase(
    private val repo: LoginRepository,
    private val callback: LoginOutputPort
) : LoginRepository.Result {

    fun login(address: String) {
        repo.login(address, this)
    }

    override fun onSuccess() {
        callback.onLoginSuccessful()
    }

    interface LoginOutputPort {
        fun onLoginSuccessful() {}
    }

}
