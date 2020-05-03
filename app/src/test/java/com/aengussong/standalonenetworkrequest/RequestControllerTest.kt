package com.aengussong.standalonenetworkrequest

import com.aengussong.standalonenetworkrequest.model.Repo
import com.aengussong.standalonenetworkrequest.model.NetworkResponse
import com.aengussong.standalonenetworkrequest.network.RequestController
import com.aengussong.standalonenetworkrequest.network.RequestMethod
import org.awaitility.Awaitility.await
import org.awaitility.kotlin.untilNotNull
import org.junit.Assert
import org.junit.Test
import java.net.URL
import java.util.concurrent.TimeUnit


class RequestControllerTest {

    @Test
    fun `make get request - should return success`() {
        val username = "aengussong"
        val repo = "StandaloneNetworkRequest"
        val api = "https://api.github.com/repos/$username/$repo"
        val url = URL(api)
        var response: NetworkResponse<Repo>? = null

        RequestController.makeRequest(RequestMethod.GET, url, Repo::class) { resp ->
            response = resp
        }

        await().atMost(RequestController.timeout.toLong(), TimeUnit.MILLISECONDS)
            .untilNotNull { response }
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
        Assert.fail("not implemented")
    }

    @Test
    fun `make request to http - should return success`(){
        Assert.fail()
    }

    @Test
    fun `make request to https - should return success`(){
        Assert.fail()
    }
}