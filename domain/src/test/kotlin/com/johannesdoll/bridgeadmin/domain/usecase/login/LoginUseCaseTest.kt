/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

import io.mockk.clearMocks
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private val repo: LoginRepository = relaxedMockk()
    private val callback: LoginUseCase.LoginOutputPort = relaxedMockk()

    @BeforeEach
    fun setUp() {
        useCase = LoginUseCase(repo, callback)
    }

    @Test
    fun `Given an network address, when login is called, repo is called with address`() {
        useCase.login("localhost:5555")

        verify {
            repo.login("localhost:5555", any())
        }
    }

    @Test
    fun `When login is called successfully, on success is called on callback`() {
        answerInRepo { result -> result.onSuccess() }

        useCase.login("localhost")

        verify {
            callback.onLoginSuccessful()
        }
    }

    private fun answerInRepo(invocation: (LoginRepository.Result) -> Unit) {
        val result = slot<LoginRepository.Result>()
        every {
            repo.login(any(), capture(result))
        } answers {
            invocation(result.captured)
        }
    }

    @AfterEach
    fun tearDown() {
        clearMocks(repo, callback)
    }
}