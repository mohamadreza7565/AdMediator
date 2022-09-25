package com.mra.admediatorsdk.data.datasource

import android.util.Log
import com.mra.admediatorsdk.data.database.RoomAppDatabase
import com.mra.admediatorsdk.data.model.Waterfall
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
    fun saveWaterfalls(waterfalls: MutableList<Waterfall>) {

        waterfalls.forEach { waterfall ->
            val calendar = Calendar.getInstance()

            waterfall.savedTimestamp = calendar.time.time

            calendar.add(Calendar.HOUR_OF_DAY, +1)
            waterfall.expireTimestamp = calendar.time.time

            roomAppDatabase.getWaterfallDao().insert(waterfall)
        }
    }


    /**
     * Get the list of waterfalls that were saved less than an hour ago
     */
    fun findAvailableWaterfalls(): List<Waterfall> {
        val timeStamp = Calendar.getInstance().time.time
        return roomAppDatabase.getWaterfallDao().getAllAvailable(timeStamp)
    }

}