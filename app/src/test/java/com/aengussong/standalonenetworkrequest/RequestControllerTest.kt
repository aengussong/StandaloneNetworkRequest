package com.aengussong.standalonenetworkrequest

import android.os.Build
import android.os.Looper.getMainLooper
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.model.Request
import com.aengussong.standalonenetworkrequest.network.RequestController
import com.aengussong.standalonenetworkrequest.network.RequestMethod
import com.aengussong.standalonenetworkrequest.network.RequestMethod.*
import com.aengussong.standalonenetworkrequest.testModels.BodyResponse
import com.aengussong.standalonenetworkrequest.testModels.Response
import com.aengussong.standalonenetworkrequest.testUtils.TEST_URL
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.net.URL

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class RequestControllerTest {

    @Test
    fun `make get request - should return success`() {
        val request = getRequest(GET)

        var getResponse: NetworkResponse<Response>? = null
        RequestController().makeRequest(
            request,
            Response::class
        ) { networkResponse: NetworkResponse<Response> ->
            getResponse = networkResponse
        }

        wait(until = { getResponse != null })
        Assert.assertTrue(getResponse?.isSuccessful ?: false)
    }

    @Test
    fun `make post request - should return success`() {
        val request = getRequest(POST, withBody = true)

        var postResponse: NetworkResponse<BodyResponse>? = null
        RequestController().makeRequest(
            request,
            BodyResponse::class
        ) { networkResponse: NetworkResponse<BodyResponse> ->
            postResponse = networkResponse
        }

        wait(until = { postResponse != null })
        Assert.assertTrue(postResponse?.isSuccessful ?: false)
        Assert.assertEquals(request.body, postResponse?.data?.data)
    }

    @Test
    fun `make put request - should return success`() {
        val request = getRequest(PUT, withBody = true)

        var putResponse: NetworkResponse<BodyResponse>? = null
        RequestController().makeRequest(
            request,
            BodyResponse::class
        ) { networkResponse: NetworkResponse<BodyResponse> ->
            putResponse = networkResponse
        }

        wait(until = { putResponse != null })
        Assert.assertTrue(putResponse?.isSuccessful ?: false)
        Assert.assertEquals(request.body, putResponse?.data?.data)
    }

    @Test
    fun `make request without network - should throw error`() {
        Assert.fail()
    }

    @Test
    fun `make request to http - should return success`() {
        Assert.fail()
    }

    @Test
    fun `make request to https - should return success`() {
        Assert.fail()
    }

    @Test
    fun `on non successful response code - should return unsuccessful response`() {
        Assert.fail()
    }

    private fun wait(millis: Long = 15_000L, until: () -> Boolean) {
        var time = 0L
        val step = 100L
        while (!until() && time < millis) {
            shadowOf(getMainLooper()).idle()
            time += step
            Thread.sleep(step)
        }

        if (time > millis && !until()) {
            throw WaitTimeIsOverException()
        }
    }

    private fun getRequest(method: RequestMethod, withBody: Boolean = false): Request {
        val api = when (method) {
            POST -> "$TEST_URL/post"
            GET -> "$TEST_URL/get"
            PUT -> "$TEST_URL/put"
        }

        val url = URL(api)
        return if (withBody) {
            val body = "{\"body\":\"body\"}"
            Request(method, url, body)
        } else {
            Request(method, url)
        }
    }

    class WaitTimeIsOverException : Exception()
}