package com.aengussong.standalonenetworkrequest

import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RequestController{

    fun makeRequest(url:URL){
        val connection = url.openConnection() as HttpsURLConnection
    }
}