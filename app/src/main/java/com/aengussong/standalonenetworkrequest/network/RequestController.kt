package com.aengussong.standalonenetworkrequest.network

import android.os.Handler
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.parser.Parser
import com.aengussong.standalonenetworkrequest.parser.ResponseParser
import com.aengussong.standalonenetworkrequest.utils.Utils.Companion.NUMBER_OF_CORES
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
/**
 * Performs network request on background thread. Request connection timeout and json parser can be adjusted.
 * */
object RequestController {

    /**
     * Request connection timeout. Can be set through [setRequestConnectionTimeout]
     * */
    var timeout: Int = 15_000
        private set

    /**
     * JSON response parser. Default implementation is [ResponseParser]. Can be set through [setJSONParser]
     * */
    var parser:Parser = ResponseParser()
        private set

    private val handler = Handler()

    private val decodeWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val executor =
        ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 1L, TimeUnit.SECONDS, decodeWorkQueue)

    /**
     * Set connection timeout for requests. This value can be overridden by requestTimeout passed to [makeRequest]
     *
     * @param timeout - request connection timeout
     * */
    fun setRequestConnectionTimeout(timeout: Int) {
        this.timeout = timeout
    }

    /**
     * Set response JSON deserializer. Must implement [Parser] interface.
     *
     * @param parser - JSON deserializer implementation
     * */
    fun setJSONParser(parser: Parser){
        this.parser = parser
    }

    /**
     * Perform network request. Does not provide any means to cancel request.
     *
     * @param requestMethod - REST HTTP method for request
     * @param url - url for request
     * @param responseKlass - connection response will be casted to this class
     * @param requestTimeout - connection request timeout, overrides [timeout] value
     * @param callback - callback to deliver connection response, stored as [WeakReference]
     * */
    fun <T:Any> makeRequest(
        requestMethod: RequestMethod,
        url: URL,
        responseKlass: KClass<T>,
        requestTimeout: Int? = null,
        callback: (NetworkResponse<T>) -> Unit
    ) {
        val weakCallback = WeakReference(callback)

        val request = NetworkRequest(
            requestTimeout ?: timeout,
            requestMethod,
            url,
            responseKlass,
            handler,
            parser,
            weakCallback
        )
        executor.execute(request)
    }
}

/**
 * HTTP methods, available to [RequestController]
 * */
enum class RequestMethod {
    GET,
    POST,
    PUT
}