package com.aengussong.standalonenetworkrequest

import android.os.Handler
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.untilNotNull
import org.junit.Assert
import org.junit.Test
import java.net.URL
import java.util.concurrent.TimeUnit


class RequestControllerTest {

    private val requestController = RequestController()

    @Test
    fun `make get request - should return success`() {
        val username = "aengussong"
        val api = "https://api.github.com/users/$username/repos"
        val url = URL(api)
        var response: ReposResponse? = null

        requestController.makeRequest<ReposResponse>(RequestMethod.GET, url){resp ->
            response = resp
        }

        await().atMost(requestController.TIMEOUT, TimeUnit.MILLISECONDS)
            .untilNotNull<ReposResponse>(response)
        Assert.assertTrue(response.isSuccessful)
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
    fun `make request with no network - should throw error`() {
        Assert.fail("not implemented")
    }
}