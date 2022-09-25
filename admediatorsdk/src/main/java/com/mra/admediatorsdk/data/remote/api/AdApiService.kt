package com.mra.admediatorsdk.data.remote.api

import com.mra.admediatorsdk.data.model.AdNetworkModel
import com.mra.admediatorsdk.data.model.WaterfallModel
import retrofit2.Response
import retrofit2.http.GET


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */

interface AdApiService {

    @GET("adNetworks")
    suspend fun getAdNetworks(): Response<AdNetworkModel>

    @GET("rewarded")
    suspend fun getWaterfalls(): Response<WaterfallModel>

}