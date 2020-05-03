package com.aengussong.standalonenetworkrequest.model

data class NetworkResponse<T>(
    val isSuccessful: Boolean,
    val responseCode: Int,
    val data: T?)