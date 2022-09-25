package com.mra.admediatorsdk

import android.app.Application
import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.mra.admediatorsdk.core.di.dataBaseModule
import com.mra.admediatorsdk.core.di.gsonModule
import com.mra.admediatorsdk.core.di.networkModule
import com.mra.admediatorsdk.core.di.repositoryModule
import com.mra.admediatorsdk.core.middleware.AdsAvailableMiddleware
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.mockito.Mock

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */


class MiddlewareTest {

    @BeforeEach
    fun ini(){
        startKoin {
            androidContext(getInstrumentation().context)
            modules(
                listOf(
                    gsonModule, networkModule, dataBaseModule, repositoryModule
                )
            )
        }
    }

    @Test
    fun checkMiddleware() = runTest{

        val middleware = AdsAvailableMiddleware(
            arrayListOf(Waterfall(id = "5caaf03dc1ed8b000149cedd", name = WaterfallName.TAPSELL.value))
        )
        middleware.check(
            onAvailableAd = { adId, zoneId, waterfallName ->
                assert(true){
                    "Find available ad"
                }
            },
            onNotAvailableAd = {
                assert(false) {
                    "Ad not available"
                }
            }
        )
    }

}