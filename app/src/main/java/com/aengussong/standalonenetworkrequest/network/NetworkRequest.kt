package com.aengussong.standalonenetworkrequest.network

import android.os.Handler
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.parser.Parser
import com.aengussong.standalonenetworkrequest.utils.Utils
import java.lang.ref.WeakReference
import java.net.URL
import javax.net.ssl.HttpsURLConnection
import kotlin.reflect.KClass

/**
 * Performs network request with specified connection [timeout], [requestMethod] on specified [url].
 * After successful connect url response will be parsed to [klass]
 *
 * @param timeout - request connection timeout
 * @param requestMethod - HTTP request method
 * @param klass - request response will be tried to be deserialized to this [KClass]
 * @param handler - adds [callback] to the message queue, should be declared with the looper of
 * thread, on which [callback] should be executed
 * @param parser - request response deserializer, will try to deserialize response to [klass]
 * @param callback - deliver request's deserialized response
 * */
class NetworkRequest<T : Any>(
    private val timeout: Int,
    private val requestMethod: RequestMethod,
    private val url: URL,
    private val klass: KClass<T>,
    private val handler: Handler,
    private val parser: Parser,
    private val callback: WeakReference<(NetworkResponse<T>) -> Unit>
) : Runnable {

    override fun run() {
        val connection = url.openConnection() as HttpsURLConnection
        try {
            connection.connectTimeout = timeout
            connection.requestMethod = requestMethod.name
            connection.connect()

            val responseCode = connection.responseCode
            val isSuccessful = responseCode == HttpsURLConnection.HTTP_OK
            var parsedData: T? = null

            //read response if connection was successful
            if (isSuccessful) {
                val data = Utils.readStream(connection.inputStream)
                parsedData = parser.parse(data ?: "", klass)
            }

            val response = NetworkResponse<T>(isSuccessful, responseCode, parsedData)
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