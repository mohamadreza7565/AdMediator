package com.mra.admediatorsdk

import android.content.Context
import com.mra.admediatorsdk.ad.manager.RequestAdDataState
import com.mra.admediatorsdk.core.di.dataBaseModule
import com.mra.admediatorsdk.core.di.gsonModule
import com.mra.admediatorsdk.core.di.networkModule
import com.mra.admediatorsdk.core.di.repositoryModule
import com.mra.admediatorsdk.ad.middleware.AdsAvailableMiddleware
import com.mra.admediatorsdk.data.model.Waterfall
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */


class MiddlewareTest {

    private val context = mockk<Context>()

    @BeforeEach
    fun initMiddlewareTest() {
        initializeKoin()
    }

    private fun initializeKoin() {
        startKoin {
            androidContext(context)
            modules(
                listOf(
                    gsonModule, networkModule, dataBaseModule, repositoryModule
                )
            )
        }
    }

    @Test
    fun testMiddleware() = runTest {
        val middleware = AdsAvailableMiddleware(
            arrayListOf(
                Waterfall(
                    id = "1",
                    name = "test"
                ),
                Waterfall(
                    id = "2",
                    name = "test2"
                )
            ),
            callback = {
                when (it) {
                    is RequestAdDataState.AvailableAd -> {
                        it.data.let {
                            assertNotEquals(it.adId, "")
                            assertNotNull(it.adId)
                            assertNotEquals(it.zoneId, "")
                            assertNotNull(it.zoneId)
                            assertNotEquals(it.waterfallName, "")
                            assertNotNull(it.waterfallName)
                        }
                    }
                    is RequestAdDataState.NotAvailableAd->{
                        assert(true) {
                            "Ad not available"
                        }
                    }
                }
            },

        )
        middleware.check()

    }


}