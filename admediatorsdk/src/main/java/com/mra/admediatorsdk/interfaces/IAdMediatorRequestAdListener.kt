package com.mra.admediatorsdk.interfaces

import com.mra.admediatorsdk.data.enums.WaterfallName

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
interface IAdMediatorRequestAdListener {

    fun onRequestAdStart()

    fun onRequestAdResponse(adId: String?, zoneId: String,waterfallName: WaterfallName)

    fun onRequestAdFailure(errorMessage: String?)

}