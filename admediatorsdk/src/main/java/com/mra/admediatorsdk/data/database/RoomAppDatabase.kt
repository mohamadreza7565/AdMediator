package com.mra.admediatorsdk.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mra.admediatorsdk.data.database.dao.WaterfallDao
import com.mra.admediatorsdk.data.model.Waterfall
import com.mra.admediatorsdk.data.model.WaterfallConverter


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */

@Database(
    entities = [
        Waterfall::class
    ],
    exportSchema = true,
    autoMigrations = [], version = 1
)
abstract class RoomAppDatabase : RoomDatabase() {

    abstract fun getWaterfallDao(): WaterfallDao

    companion object {
        const val DATABASE_NAME = "db_ad_mediator_sdk"
    }

}

fun createDataBaseInstance(context: Context): RoomAppDatabase {
    return Room.databaseBuilder(
        context,
        RoomAppDatabase::class.java,
        RoomAppDatabase.DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

}
