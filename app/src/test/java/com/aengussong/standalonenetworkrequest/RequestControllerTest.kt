package com.aengussong.standalonenetworkrequest

import org.junit.Assert
import org.junit.Test
import java.net.URL


class RequestControllerTest {

    private val requestController = RequestController()

    @Test
    fun `make request - should return success`(){
        val username = "aengussong"
        val api = "https://api.github.com/users/$username/repos"
        val url = URL(api)

        val response:ReposResponse = requestController.makeRequest(url)

        Assert.assertTrue(response.isSuccessful)
    }

    @Test
    fun `make request with no network - should throw error`(){
        Assert.fail("not implemented")
    }
}