package com.mra.admediatorsdk


import android.util.Log
import com.google.gson.GsonBuilder
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.core.di.createBaseNetworkClient
import com.mra.admediatorsdk.data.remote.api.AdApiService
import com.mra.admediatorsdk.data.repo.AdRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ApisTest {

    private lateinit var api : AdApiService

    @BeforeEach
    fun initApiTest(){
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