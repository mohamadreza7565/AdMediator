package com.mra.admediatorsdk

import android.content.Context
import android.util.Log
import com.mra.admediatorsdk.core.AdMediaSdkApplication
import com.mra.admediatorsdk.core.di.dataBaseModule
import com.mra.admediatorsdk.core.di.gsonModule
import com.mra.admediatorsdk.core.di.networkModule
import com.mra.admediatorsdk.core.di.repositoryModule
import com.mra.admediatorsdk.core.middleware.AdsAvailableMiddleware
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds
import io.mockk.mockk
import junit.runner.Version.main
import junit.textui.TestRunner.main
import kotlinx.coroutines.test.runTest
import net.bytebuddy.agent.Attacher.main
import net.bytebuddy.implementation.MethodCall.run
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mockito.MockedStatic
import org.mockito.MockedStatic.Verification
import org.mockito.Mockito


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
                )
            )
        )
        middleware.check(
            onAvailableAd = { adId, zoneId, waterfallName ->
                assertNotEquals(adId, "")
                assertNotNull(adId)
                assertNotEquals(zoneId, "")
                assertNotNull(zoneId)
                assertNotEquals(waterfallName, "")
                assertNotNull(waterfallName)
            },
            onNotAvailableAd = {
                assert(true) {
                    "Ad not available"
                }
            }
        )

    }


}