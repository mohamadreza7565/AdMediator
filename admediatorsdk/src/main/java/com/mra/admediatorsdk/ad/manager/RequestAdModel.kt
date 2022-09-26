package com.mra.admediatorsdk.ad.manager

import com.mra.admediatorsdk.data.enums.WaterfallName

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
data class RequestAdModel(
    var waterfallName: WaterfallName,
    var adId: String?,
    var zoneId: String
)