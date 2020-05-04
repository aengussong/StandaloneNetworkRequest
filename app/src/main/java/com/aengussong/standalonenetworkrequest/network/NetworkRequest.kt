package com.aengussong.standalonenetworkrequest.network

import android.os.Handler
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.model.Request
import com.aengussong.standalonenetworkrequest.network.RequestController.timeout
import com.aengussong.standalonenetworkrequest.parser.Parser
import com.aengussong.standalonenetworkrequest.utils.Utils
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URLConnection
import javax.net.ssl.HttpsURLConnection
import kotlin.reflect.KClass

/**
 * Performs network request with specified connection params, passed in [request] object.
 * After successful connect url response will be parsed to [klass]
 *
 * @param request - request params
 * @param klass - request response will be tried to be deserialized to this [KClass]
 * @param handler - adds [callback] to the message queue, should be declared with the looper of
 * thread, on which [callback] should be executed
 * @param parser - request response deserializer, will try to deserialize response to [klass]
 * @param callback - deliver request's deserialized response
 * */
class NetworkRequest<T : Any>(
    private val request: Request,
    private val klass: KClass<T>,
    private val timout: Int,
    private val handler: Handler,
    private val parser: Parser,
    private val callback: WeakReference<(NetworkResponse<T>) -> Unit>
) : Runnable {

    override fun run() {
        val connection = request.url.openConnection() as HttpURLConnection
        try {
            connection.connectTimeout = request.timeout ?: timeout
            connection.requestMethod = request.method.name

            //send request body if available
            request.body?.let { body ->
                connection.setFixedLengthStreamingMode(body.length)
                connection.doOutput = true
                connection.setRequestProperty("Content-Type","application/json");
                Utils.writeToStream(connection.outputStream, body)
            } ?: run {
                connection.connect()
            }

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

}