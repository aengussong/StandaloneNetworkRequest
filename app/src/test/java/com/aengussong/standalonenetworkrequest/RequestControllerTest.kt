package com.aengussong.standalonenetworkrequest

import android.os.Build
import android.os.Looper.getMainLooper
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.model.Repo
import com.aengussong.standalonenetworkrequest.network.RequestController
import com.aengussong.standalonenetworkrequest.network.RequestMethod
import org.awaitility.Awaitility.await
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowLooper
import java.net.URL
import java.util.concurrent.TimeUnit

@Config(sdk = [Build.VERSION_CODES.P])
@RunWith(RobolectricTestRunner::class)
class RequestControllerTest {

    @Test
    fun `make get request - should return success`() {
        val username = "aengussong"
        val repo = "StandaloneNetworkRequest"
        val api = "https://api.github.com/repos/$username/$repo"
        val url = URL(api)
        var response: NetworkResponse<Repo>? = null
        ShadowLooper.shadowMainLooper()

        RequestController.makeRequest(RequestMethod.GET, url, Repo::class) { resp ->
            response = resp
        }

        await().atMost(RequestController.timeout.toLong(), TimeUnit.MILLISECONDS).then()
            .until {
                shadowOf(getMainLooper()).idle()
                response != null
            }
        Assert.assertTrue(response?.isSuccessful ?: false)
    }

    @Test
    fun `make post request - should return success`() {
        Assert.fail()
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