package com.mra.admediatorsdk.data.database.dao

import androidx.room.*
import com.mra.admediatorsdk.data.model.Waterfall

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */

@Dao
interface WaterfallDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(waterfall: Waterfall): Long

    @Query("SELECT * FROM TBL_WATERFALL WHERE name = :name")
    fun get(name: String): Waterfall

    @Query("SELECT * FROM TBL_WATERFALL")
    fun getAll(): List<Waterfall>

    @Query("SELECT * FROM TBL_WATERFALL WHERE expireTimestamp >= :timeStamp")
    fun getAllAvailable(timeStamp: Long): List<Waterfall>

    @Delete
    fun delete(waterfall: Waterfall)

    @Query("DELETE FROM TBL_WATERFALL")
    fun nukeTable()

}