package com.mra.admediatorsdk


import com.google.gson.GsonBuilder
import com.mra.admediatorsdk.core.di.createBaseNetworkClient
import com.mra.admediatorsdk.core.middleware.AdsAvailableMiddleware
import com.mra.admediatorsdk.data.model.Waterfall
import com.mra.admediatorsdk.data.remote.api.AdApiService
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.util.concurrent.TimeUnit


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class ApisTest {

    private lateinit var api: AdApiService

    @BeforeEach
    fun initApiTest() {
        api = createBaseNetworkClient(GsonBuilder().create(), BuildConfig.TAPSELL_BASE_URL).create(
            AdApiService::class.java
        )
    }


    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    fun getAdNetworksTest() = runTest {
        val response = api.getAdNetworks()
        assertEquals(response.code(), 200)
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    fun getWaterfallsTest() = runTest {
        val response = api.getWaterfalls()
        assertEquals(response.code(), 200)
    }


}