package com.aengussong.standalonenetworkrequest

import android.os.Build
import android.os.Looper.getMainLooper
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.model.Request
import com.aengussong.standalonenetworkrequest.network.RequestController
import com.aengussong.standalonenetworkrequest.network.RequestMethod
import com.aengussong.standalonenetworkrequest.testModels.PostResponse
import com.aengussong.standalonenetworkrequest.testModels.Repo
import org.awaitility.Awaitility.await
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import java.net.URL
import java.util.concurrent.TimeUnit

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class RequestControllerTest {

    //todo Flaky test!!!!!!!!!!
    @Test
    fun `make get request - should return success`() {
        val username = "aengussong"
        val repo = "StandaloneNetworkRequest"
        val api = "https://api.github.com/repos/$username/$repo"
        val url = URL(api)
        val request = Request(RequestMethod.GET, url)
        shadowOf(getMainLooper())

        var response: NetworkResponse<Repo>? = null

        RequestController.makeRequest(request, Repo::class) { networkResponse ->
            response = networkResponse
        }

        await().atMost(RequestController.timeout.toLong(), TimeUnit.MILLISECONDS)
            .until {
                shadowOf(getMainLooper()).runOneTask()
                response != null
            }
        Assert.assertTrue(response?.isSuccessful ?: false)
    }

    //todo Flaky test
    @Test
    fun `make post request - should return success`() {
        val api = "http://httpbin.org/post"
        val url = URL(api)
        val body = "{\"test\":\"test\"}"
        val request = Request(RequestMethod.POST, url, body)
        var response: NetworkResponse<PostResponse>? = null
        shadowOf(getMainLooper())


        RequestController.makeRequest(request, PostResponse::class) { networkResponse ->
            response = networkResponse
        }

        await().atMost(RequestController.timeout.toLong(), TimeUnit.MILLISECONDS)
            .until {
                shadowOf(getMainLooper()).runOneTask()
                response != null
            }
        Assert.assertTrue(response?.isSuccessful ?: false)
        Assert.assertEquals(body, response?.data?.data)
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
}