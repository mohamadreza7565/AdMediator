package com.mra.admediatorsdk.data.repo

import android.util.Log
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.data.datasource.AdLocalDataSource
import com.mra.admediatorsdk.data.datasource.AdRemoteDataSource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class AdRepo(
    private val remoteDataSource: AdRemoteDataSource,
    private val localDataSource: AdLocalDataSource
) {

    suspend fun getAdNetworks() = remoteDataSource.getAdNetworks()

    suspend fun getRewardedWaterfall() = flow {

//        .map { _result ->
//        if (_result.status == CustomResult.Status.SUCCESS) {
//            _result.data?.let {
//                localDataSource.saveWaterfalls(it.waterfall)
//            }
//        }
//    }

        remoteDataSource.getRewardedWaterfall().collect { _result ->
            if (_result.status == CustomResult.Status.SUCCESS)
                _result.data?.let {
                    localDataSource.saveWaterfalls(it.waterfalls)
                }
            emit(_result)
        }
    }

    fun getAvailableRewardedWaterfall() = localDataSource.findAvailableWaterfalls()

}