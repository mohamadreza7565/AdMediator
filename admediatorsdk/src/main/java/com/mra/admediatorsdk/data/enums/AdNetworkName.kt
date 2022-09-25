package com.mra.admediatorsdk.data.enums

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
enum class AdNetworkName(val value: String) : Serializable {

    @SerializedName("UnityAds")
    UNIT_ADS("UnityAds"),

    @SerializedName("Tapsell")
    TAPSELL("Tapsell"),

}