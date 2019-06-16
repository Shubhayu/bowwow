package com.synchroniverse.bowwow.errorhandling

data class BWError(val code: String, val message: String, val cause: Throwable? = null)
