package com.mra.admediatorsdk.data.datasource

import android.util.Log
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.data.database.RoomAppDatabase
import com.mra.admediatorsdk.data.enums.WaterfallType
import com.mra.admediatorsdk.data.model.Waterfall
import com.mra.admediatorsdk.data.model.WaterfallModel
import com.mra.admediatorsdk.global.utils.convertListToArrayList
import kotlinx.coroutines.flow.flow
import java.util.*

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  The operations related to the program's internal memory are in this class
 *
 */
class AdLocalDataSource(private val roomAppDatabase: RoomAppDatabase) {


    /**
     * Set savedTimeStamp and expireTimeStamp for waterfalls
     * and
     * save they into database
     */
    fun saveWaterfalls(waterfalls: MutableList<Waterfall>): CustomResult<WaterfallModel> {

        val dao = roomAppDatabase.getWaterfallDao()
        dao.nukeTable()
        waterfalls.forEach { waterfall ->
            val calendar = Calendar.getInstance()

            waterfall.savedTimestamp = calendar.time.time

            calendar.add(Calendar.HOUR_OF_DAY, +1)
            waterfall.expireTimestamp = calendar.time.time

            dao.insert(waterfall)
        }
        return findAvailableWaterfalls()
    }


    /**
     * Get the list of waterfalls that were saved less than an hour ago
     */
    fun findAvailableWaterfalls(): CustomResult<WaterfallModel> {
        val timeStamp = Calendar.getInstance().time.time
        val availableList =
            roomAppDatabase.getWaterfallDao().getAllAvailable(timeStamp).convertListToArrayList()
        return CustomResult.success(WaterfallModel(WaterfallType.REWARDED, availableList))
    }

}