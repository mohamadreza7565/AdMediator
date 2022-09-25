package com.mra.admediatorsdk.core.middleware

import android.content.Context
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall
import com.unity3d.ads.IUnityAdsLoadListener
import com.unity3d.ads.UnityAds
import ir.tapsell.sdk.Tapsell
import ir.tapsell.sdk.TapsellAdRequestListener
import ir.tapsell.sdk.TapsellAdRequestOptions
import org.koin.java.KoinJavaComponent.inject

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  Gets the list of waterfalls and checks if this ad is available or not
 *
 */
class AdsAvailableMiddleware(private val waterfalls: ArrayList<Waterfall>) : CheckAdsMiddleware() {

    private val mContext: Context by inject(Context::class.java)

    /**
     * Check ad is available or not
     *
     *
     *
     */
    override fun check(
        onAvailableAd: (adId: String, zoneId: String, waterfallName: WaterfallName) -> Unit,
        onNotAvailableAd: () -> Unit,
    ) {
        var findAvailableAd = false
        var position = 0
        do {
            requestAds(waterfalls[position]) { available, adId ->
                if (available && adId != null && WaterfallName.values()
                        .find { it.value == waterfalls[position].name } != null
                ) {
                    findAvailableAd = true
                    onAvailableAd.invoke(
                        adId,
                        waterfalls[position].id,
                        WaterfallName.values().find { it.value == waterfalls[position].name }!!
                    )
                } else {
                    position++
                    if (position == waterfalls.size) {
                        onNotAvailableAd.invoke()
                    }
                }
            }
        } while (!findAvailableAd && position < waterfalls.size)

    }


    private fun requestAds(
        waterfall: Waterfall,
        callback: (available: Boolean, adId: String?) -> Unit
    ) {
        when (waterfall.name) {
            WaterfallName.UNIT_ADS.value -> requestUnityAds(waterfall.id) { available, adId ->
                callback.invoke(available, adId)
            }

            WaterfallName.TAPSELL.value -> requestTapsellAd(waterfall.id) { available, adId ->
                callback.invoke(available, adId)
            }
        }
    }


    private fun requestTapsellAd(
        zoneId: String,
        callback: (available: Boolean, adId: String?) -> Unit
    ) {

        Tapsell.requestAd(
            mContext,
            zoneId,
            TapsellAdRequestOptions(),
            object : TapsellAdRequestListener() {
                override fun onAdAvailable(adId: String) {
                    callback.invoke(true, adId)
                }

                override fun onError(message: String) {
                    callback.invoke(false, null)
                }
            })
    }

    private fun requestUnityAds(
        zoneId: String,
        callback: (available: Boolean, adId: String?) -> Unit
    ) {

        if (UnityAds.isInitialized())
            UnityAds.load(zoneId, iUnityAdsLoadListener { available, adId ->
                callback.invoke(available, adId)
            })
        else
            callback.invoke(false, null)


    }

    private fun iUnityAdsLoadListener(
        callback: (available: Boolean, adId: String?) -> Unit
    ) =
        object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(adId: String?) {
                callback.invoke(true, adId)
            }

            override fun onUnityAdsFailedToLoad(
                p0: String?,
                p1: UnityAds.UnityAdsLoadError?,
                p2: String?
            ) {
                callback.invoke(false, null)
            }

        }


}
