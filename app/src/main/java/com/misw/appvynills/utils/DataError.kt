package com.misw.appvynills.utils

data class DataError(
    override val message: String,
    val statusCode: Int? = null
): Exception()
