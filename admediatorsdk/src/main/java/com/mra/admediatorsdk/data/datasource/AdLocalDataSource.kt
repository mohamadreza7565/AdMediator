package com.mra.admediatorsdk.data.datasource

import android.util.Log
import com.mra.admediatorsdk.data.database.RoomAppDatabase
import com.mra.admediatorsdk.data.model.Waterfall
import java.util.*

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class AdLocalDataSource(private val roomAppDatabase: RoomAppDatabase) {


    fun saveWaterfalls(waterfalls: MutableList<Waterfall>) {

        waterfalls.forEach { waterfall ->
            val calendar = Calendar.getInstance()

            waterfall.savedTimestamp = calendar.time.time

            calendar.add(Calendar.HOUR_OF_DAY, +1)
            waterfall.expireTimestamp = calendar.time.time

            roomAppDatabase.getWaterfallDao().insert(waterfall)
        }
    }

    fun findAvailableWaterfalls(): List<Waterfall> {
        val timeStamp = Calendar.getInstance().time.time
        return roomAppDatabase.getWaterfallDao().getAllAvailable(timeStamp)
    }

}