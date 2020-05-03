package com.aengussong.standalonenetworkrequest.network

import android.os.Handler
import com.aengussong.standalonenetworkrequest.model.RequestResponse
import com.aengussong.standalonenetworkrequest.utils.Utils.Companion.NUMBER_OF_CORES
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object RequestController {

    var timeout: Int = 15_000
        private set

    private val handler = Handler()

    private val decodeWorkQueue: BlockingQueue<Runnable> = LinkedBlockingQueue()
    private val executor =
        ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES, 1L, TimeUnit.SECONDS, decodeWorkQueue)

    fun setGlobalTimeout(timeout: Int) {
        this.timeout = timeout
    }

    fun <T> makeRequest(
        requestMethod: RequestMethod,
        url: URL,
        requestTimeout: Int? = null,
        callback: (RequestResponse<T>) -> Unit
    ) {
        val weakCallback = WeakReference(callback)

        val request = NetworkRequest(
            requestTimeout ?: timeout,
            requestMethod,
            url,
            handler,
            weakCallback
        )
        executor.execute(request)
    }
}

enum class RequestMethod {
    GET,
    POST,
    PUT
}