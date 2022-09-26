package com.mra.admediatorsdk.data.repo

import android.util.Log
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.data.datasource.AdLocalDataSource
import com.mra.admediatorsdk.data.datasource.AdRemoteDataSource
import com.mra.admediatorsdk.data.enums.WaterfallType
import com.mra.admediatorsdk.data.model.WaterfallModel
import com.mra.admediatorsdk.global.utils.convertListToArrayList
import kotlinx.coroutines.flow.*

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class AdRepo(
    private val remoteDataSource: AdRemoteDataSource,
    private val localDataSource: AdLocalDataSource
) {

    private val TAG = "AD_REPO"

    suspend fun getAdNetworks() = remoteDataSource.getAdNetworks()

    suspend fun getRewardedWaterfall() = flow {

        val dbData = getDbAvailableWaterfall()
        if (dbData.data!!.waterfalls.isEmpty()) {
            Log.i(TAG, "get waterfalls from server")
            remoteDataSource.getRewardedWaterfall().map {
                if (it.status == CustomResult.Status.SUCCESS) {
                    localDataSource.saveWaterfalls(it.data!!.waterfalls)
                } else {
                    it
                }
            }.collect {
                emit(it)
            }
        } else {
            Log.i(TAG, "get waterfalls from db")
            emit(CustomResult.loading())
            emit(dbData)
        }

    }

    private fun getDbAvailableWaterfall() = localDataSource.findAvailableWaterfalls()

}