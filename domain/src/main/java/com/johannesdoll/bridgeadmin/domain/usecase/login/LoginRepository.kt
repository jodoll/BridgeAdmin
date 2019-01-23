/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

interface LoginRepository {

    fun login(address: String, callback: Result)

    interface Result {
        fun onSuccess()
    }
}
