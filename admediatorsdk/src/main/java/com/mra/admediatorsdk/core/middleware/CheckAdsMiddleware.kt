package com.mra.admediatorsdk.core.middleware

import com.mra.admediatorsdk.data.enums.WaterfallName

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */


/**
 * Base middleware class for check ad
 */
abstract class CheckAdsMiddleware {

    private var next: CheckAdsMiddleware? = null

    companion object {
        fun newInstance(middleware: CheckAdsMiddleware): CheckAdsMiddleware {
            return middleware
        }
    }


    abstract fun check(
        onAvailableAd: (adId: String, zoneId: String, waterfallName: WaterfallName) -> Unit,
        onNotAvailableAd: () -> Unit,
    )

}