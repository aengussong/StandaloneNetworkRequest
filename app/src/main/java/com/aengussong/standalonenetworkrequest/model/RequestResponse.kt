package com.aengussong.standalonenetworkrequest.model

data class RequestResponse<T>(
    val isSuccessful: Boolean,
    val responseCode: Int,
    val data: T?)