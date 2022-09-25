package com.mra.admediatorsdk.core

import android.app.Application
import android.util.Log
import com.mra.admediatorsdk.BuildConfig
import com.mra.admediatorsdk.core.di.*
import com.unity3d.ads.IUnityAdsInitializationListener
import com.unity3d.ads.UnityAds
import ir.tapsell.sdk.Tapsell
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class AdMediaSdkApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initializeTapsell()
        initializeUnityAds()
        initializeKoin()
    }


    /**
     * Initialize dependency injection with koin library
     */
    private fun initializeKoin() {
        startKoin {
            androidContext(this@AdMediaSdkApplication)
            modules(
                listOf(
                    gsonModule, networkModule, dataBaseModule, repositoryModule
                )
            )
        }
    }


    /**
     * Initialize unity ads library
     */
    private fun initializeUnityAds() {
        UnityAds.initialize(
            this,
            BuildConfig.UNITY_GAME_ID,
            true,
            object : IUnityAdsInitializationListener {
                override fun onInitializationComplete() {
                    Log.i("initializeUnityAds", "onInitializationComplete")
                }

                override fun onInitializationFailed(
                    p0: UnityAds.UnityAdsInitializationError?,
                    p1: String?
                ) {
                    Log.i(
                        "initializeUnityAds",
                        "Ad unity : " + p0?.name + ", error: " + p1
                    )
                }

            })
    }


    /**
     * Initialize tapsell library
     */
    private fun initializeTapsell() {
        Tapsell.initialize(
            this,
            BuildConfig.TAPSELL_TOKEN
        )
    }


}