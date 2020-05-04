package com.aengussong.standalonenetworkrequest.model

import com.aengussong.standalonenetworkrequest.network.RequestMethod
import java.net.URL

data class Request(
    val method: RequestMethod,
    val url: URL,
    val body: String? = null,
    val timeout: Int? = null
)