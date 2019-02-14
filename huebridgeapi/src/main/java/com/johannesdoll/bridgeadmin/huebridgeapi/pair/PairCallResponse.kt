/*
 * Copyright (c) 2019 Johannes Doll. All rights reserved.
 */

package com.johannesdoll.bridgeadmin.huebridgeapi.pair

sealed class PairCallResponse {

    object PairingNeeded : PairCallResponse()

    class PairingSuccessful : PairCallResponse()
}
