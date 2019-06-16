package com.synchroniverse.bowwow.services

import com.synchroniverse.bowwow.errorhandling.BWError

class BWResponse<T>(val response: T? = null, val error: BWError? = null) {

    fun isSuccess() = response != null
}