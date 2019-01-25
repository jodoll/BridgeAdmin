/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.login

import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class LoginUseCaseTest {

    private lateinit var useCase: LoginUseCase
    private val remoteRepository: LoginRepository = relaxedMockk()
    private val credentialsRepo: LoginCredentialsRepository = relaxedMockk()
    private val callback: LoginUseCase.LoginOutputPort = relaxedMockk()

    @BeforeEach
    fun setUp() {
        useCase = LoginUseCase(remoteRepository, credentialsRepo, callback)
    }

    @Test
    fun `Given an network address, when login is called, repo is called with address`() {
        useCase.login("localhost:5555")

        verify {
            remoteRepository.login("localhost:5555", any())
        }
    }

    @Test
    fun `When login is called successfully, on success is called on callback`() {
        answerInRepo { result -> result.onSuccess(mockk()) }

        useCase.login("localhost")

        verify {
            callback.onLoginSuccessful()
        }
    }

    @Test
    fun `When login fails, on error is called`() {
        answerInRepo { result -> result.onError() }

        useCase.login("localhost")

        verify {
            callback.onLoginFailed()
        }
    }

    @Test
    fun `When login requests challenge, on show Challenge is called`() {
        answerInRepo { result -> result.onChallengePresented() }

        useCase.login("localhost")

        verify {
            callback.onChallengePresented()
        }
    }

    @Test
    fun `When login was successful, credentials are stored`() {
        val credentials: LoginCredentialsRepository.Credentials = mockk()
        answerInRepo { result -> result.onSuccess(credentials) }

        useCase.login("localhost")

        verify {
            credentialsRepo.storeCredentials(credentials)
        }
    }

    private fun answerInRepo(invocation: (LoginRepository.Result) -> Unit) {
        val result = slot<LoginRepository.Result>()
        every {
            remoteRepository.login(any(), capture(result))
        } answers {
            invocation(result.captured)
        }
    }

    @AfterEach
    fun tearDown() {
        clearMocks(remoteRepository, callback)
    }
}