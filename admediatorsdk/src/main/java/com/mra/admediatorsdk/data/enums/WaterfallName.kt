package com.mra.admediatorsdk.data.enums

import com.google.gson.annotations.SerializedName

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
enum class WaterfallName(var value: String){

    @SerializedName("UnityAds")
    UNIT_ADS("UnityAds"),

    @SerializedName("Tapsell")
    TAPSELL("Tapsell"),

}