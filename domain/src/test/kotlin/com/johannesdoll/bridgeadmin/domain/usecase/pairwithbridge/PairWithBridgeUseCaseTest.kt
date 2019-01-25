/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.domain.usecase.pairwithbridge

import com.johannesdoll.bridgeadmin.test.relaxedMockk
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PairWithBridgeUseCaseTest {

    private lateinit var useCase: PairWithBridgeUseCase
    private val remoteRepository: PairWithBridgeRepository = relaxedMockk()
    private val credentialsRepo: PairWithBridgeCredentialsRepository = relaxedMockk()
    private val callback: PairWithBridgeUseCase.OutputPort = relaxedMockk()

    @BeforeEach
    fun setUp() {
        useCase = PairWithBridgeUseCase(remoteRepository, credentialsRepo, callback)
    }

    @Test
    fun `Given an network address, when pair is called, repo is called with address`() {
        useCase.pair("localhost:5555")

        verify {
            remoteRepository.pair("localhost:5555", any())
        }
    }

    @Test
    fun `When pair is called successfully, on success is called on callback`() {
        answerInRepo { result -> result.onSuccess(mockk()) }

        useCase.pair("localhost")

        verify {
            callback.onPairingSuccessful()
        }
    }

    @Test
    fun `When pair fails, on error is called`() {
        answerInRepo { result -> result.onError() }

        useCase.pair("localhost")

        verify {
            callback.onPairingFailed()
        }
    }

    @Test
    fun `When pair requests challenge, on show Challenge is called`() {
        answerInRepo { result -> result.onChallengePresented() }

        useCase.pair("localhost")

        verify {
            callback.onChallengePresented()
        }
    }

    @Test
    fun `When pair was successful, credentials are stored`() {
        val credentials: PairWithBridgeCredentialsRepository.Credentials = mockk()
        answerInRepo { result -> result.onSuccess(credentials) }

        useCase.pair("localhost")

        verify {
            credentialsRepo.storeCredentials(credentials)
        }
    }

    private fun answerInRepo(invocation: (PairWithBridgeRepository.Result) -> Unit) {
        val result = slot<PairWithBridgeRepository.Result>()
        every {
            remoteRepository.pair(any(), capture(result))
        } answers {
            invocation(result.captured)
        }
    }

    @AfterEach
    fun tearDown() {
        clearMocks(remoteRepository, callback)
    }
}