package com.mra.admediatorsdk.data.model


import com.google.gson.annotations.SerializedName
import com.mra.admediatorsdk.data.enums.AdNetworkName

data class AdNetworkModel(
    @SerializedName("adNetworks")
    val adNetworks: ArrayList<AdNetwork>
)

data class AdNetwork(

    @SerializedName("id")
    val id: String,

    @SerializedName("name")
    val name: AdNetworkName

)