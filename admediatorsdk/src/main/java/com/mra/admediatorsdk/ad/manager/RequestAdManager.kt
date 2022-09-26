package com.mra.admediatorsdk.ad.manager

import android.content.Context
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.UnityAds
import ir.tapsell.sdk.Tapsell
import ir.tapsell.sdk.TapsellAdRequestListener
import ir.tapsell.sdk.TapsellAdRequestOptions
import org.koin.java.KoinJavaComponent

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
object RequestAdManager {

    private val mContext: Context by KoinJavaComponent.inject(Context::class.java)

    /**
     * Request to Tapsell to get promotion information
     */
    fun requestTapsellAd(
        zoneId: String,
        callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
    ) {
        Tapsell.requestAd(
            mContext,
            zoneId,
            TapsellAdRequestOptions(),
            iTapsellRequestAdListener(zoneId, callback)
        )
    }


    private fun iTapsellRequestAdListener(
        zoneId: String,
        callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
    ) =
        object : TapsellAdRequestListener() {
            override fun onAdAvailable(adId: String) {
                val model = RequestAdModel(
                    WaterfallName.TAPSELL,
                    adId,
                    zoneId
                )
                callback.invoke(RequestAdDataState.AvailableAd(model))
            }

            override fun onError(message: String) {
                callback.invoke(RequestAdDataState.NotAvailableAd(message))
            }
        }


    /**
     * Request to UnityAds to get promotion information
     */
    fun requestUnityAds(
        zoneId: String,
        callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
    ) {

        if (UnityAds.isInitialized())
            UnityAds.load(zoneId, iUnityAdsLoadListener(zoneId, callback))
        else
            callback.invoke(RequestAdDataState.NotAvailableAd("Unity ad not initialized"))


    }

    fun iUnityAdsLoadListener(
        zoneId: String,
        callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
    ) =
        object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(adId: String?) {
                val model = RequestAdModel(
                    WaterfallName.UNIT_ADS,
                    adId,
                    zoneId
                )
                callback.invoke(RequestAdDataState.AvailableAd(model))
            }

            override fun onUnityAdsFailedToLoad(
                message: String?,
                p1: UnityAds.UnityAdsLoadError?,
                p2: String?
            ) {
                callback.invoke(RequestAdDataState.NotAvailableAd(message))
            }

        }

}