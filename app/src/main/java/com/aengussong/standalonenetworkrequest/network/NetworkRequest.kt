package com.aengussong.standalonenetworkrequest.network

import android.os.Handler
import com.aengussong.standalonenetworkrequest.model.RequestResponse
import com.aengussong.standalonenetworkrequest.utils.Utils
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class NetworkRequest<T>(
    private val timeout: Int,
    private val requestMethod: RequestMethod,
    private val url: URL,
    private val handler: Handler,
    private val callback: WeakReference<(RequestResponse<T>) -> Unit>
) : Runnable {

    override fun run() {
        val connection = url.openConnection() as HttpsURLConnection
        try {
            connection.connectTimeout = timeout
            connection.requestMethod = requestMethod.name
            connection.connect()
            val data = Utils.readStream(connection.inputStream)
            val parsedData: T? = Utils.parseString(data ?: "")
            val isSuccessful = true
            val responseCode = 200
            val response = RequestResponse<T>(isSuccessful, responseCode, parsedData)
            handler.post { callback.get()?.invoke(response) }
        } finally {
            connection.inputStream.close()
            connection.disconnect()
        }
    }

    private fun post(connection: HttpsURLConnection, body: String) {
//        connection.setFixedLengthStreamingMode(body.length)
//        connection.doOutput = true
//        connection.outputStream.write()
    }

}