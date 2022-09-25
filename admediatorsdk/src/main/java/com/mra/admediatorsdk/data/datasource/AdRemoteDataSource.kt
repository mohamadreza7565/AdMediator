package com.mra.admediatorsdk.data.datasource

import android.content.Context
import com.mra.admediatorsdk.core.base.BaseCoroutinesDataSource
import com.mra.admediatorsdk.data.remote.api.AdApiService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class AdRemoteDataSource(mContext: Context, private val adApiService: AdApiService) :
    BaseCoroutinesDataSource(mContext) {

    suspend fun getAdNetworks() = flow {

        callApi {
            adApiService.getAdNetworks()
        }.collect { _result ->
            emit(_result)
        }

    }

    suspend fun getRewardedWaterfall() = flow {

        callApi {
            adApiService.getWaterfalls()
        }.collect { _result ->
            emit(_result)
        }

    }

}