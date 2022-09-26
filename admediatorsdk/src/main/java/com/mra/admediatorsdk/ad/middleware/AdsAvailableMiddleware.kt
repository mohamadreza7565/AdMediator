package com.mra.admediatorsdk.ad.middleware

import com.mra.admediatorsdk.ad.manager.RequestAdManager
import com.mra.admediatorsdk.ad.manager.RequestAdDataState
import com.mra.admediatorsdk.ad.manager.RequestAdModel
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  Gets the list of waterfalls and checks if this ad is available or not
 *
 */
class AdsAvailableMiddleware(
    private val waterfalls: ArrayList<Waterfall>,
    private val callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
) : CheckAdsMiddleware() {

    private val TAG = "AD_AVAILABLE"

    /**
     * Check ad is available or not
     *
     * A loop runs as long as the number of waterfalls list and continues until one of the following conditions occurs:
     * 1- Find an available ad
     * 2- Check all the items in the list and find nothing
     */
    override fun check(position: Int) {

        requestAds(
            waterfalls[position],
            callback = {
                when (it) {
                    is RequestAdDataState.AvailableAd -> {
                        callback.invoke(it)
                    }
                    is RequestAdDataState.NotAvailableAd -> {
                        if (position == waterfalls.size) {
                            callback.invoke(it)
                        } else {
                            checkNext(position + 1)
                        }
                    }
                }
            })

    }

}


/**
 * Checking the type of waterfall and requesting it to receive advertisement information
 */
private fun requestAds(
    waterfall: Waterfall,
    callback: (state: RequestAdDataState<RequestAdModel>) -> Unit
) {
    when (waterfall.name) {
        WaterfallName.UNIT_ADS.value -> RequestAdManager.requestUnityAds(waterfall.id, callback)

        WaterfallName.TAPSELL.value -> RequestAdManager.requestTapsellAd(waterfall.id, callback)

        else -> callback.invoke(RequestAdDataState.NotAvailableAd("Waterfall is not valid"))
    }
}

