package com.aengussong.standalonenetworkrequest

import android.os.Build
import android.os.Looper.getMainLooper
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.model.Request
import com.aengussong.standalonenetworkrequest.network.RequestController
import com.aengussong.standalonenetworkrequest.network.RequestMethod
import com.aengussong.standalonenetworkrequest.testModels.GetResponse
import com.aengussong.standalonenetworkrequest.testModels.PostResponse
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
        val api = "http://httpbin.org/get"
        val url = URL(api)
        val request = Request(RequestMethod.GET, url)

        var getResponse: NetworkResponse<GetResponse>? = null

        RequestController().makeRequest(request, GetResponse::class){ networkResponse: NetworkResponse<GetResponse> ->
            getResponse = networkResponse
        }

        waitFor(15_000, until = { getResponse != null })

        Assert.assertTrue(getResponse?.isSuccessful ?: false)
    }

    @Test
    fun `make post request - should return success`() {
        val api = "http://httpbin.org/post"
        val url = URL(api)
        val body = "{\"test\":\"test\"}"
        val request = Request(RequestMethod.POST, url, body)
        var postResponse: NetworkResponse<PostResponse>? = null

        RequestController().makeRequest(
            request,
            PostResponse::class
        ) { networkResponse: NetworkResponse<PostResponse> ->
            postResponse = networkResponse
        }

        waitFor(15_000, until = { postResponse != null })

        Assert.assertTrue(postResponse?.isSuccessful ?: false)
        Assert.assertEquals(body, postResponse?.data?.data)
    }

    @Test
    fun `make put request - should return success`() {
        Assert.fail()
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

    private fun waitFor(millis: Long, until: () -> Boolean) {
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

    class WaitTimeIsOverException : Exception()
}