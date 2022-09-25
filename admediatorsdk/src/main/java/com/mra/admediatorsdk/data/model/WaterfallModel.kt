package com.mra.admediatorsdk.data.model


import androidx.room.*
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.mra.admediatorsdk.data.database.conerter.toEnum
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.enums.WaterfallType
import org.jetbrains.annotations.NotNull

data class WaterfallModel(

    @SerializedName("type")
    val type: WaterfallType,

    @SerializedName("waterfall")
    val waterfalls: ArrayList<Waterfall>

)

@Entity(tableName = "tbl_waterfall")
data class Waterfall(

    @SerializedName("id")
    @Expose
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: String = "",

    @SerializedName("name")
    @Expose
    @ColumnInfo(name = "name")
    @NotNull
    var name: String,

    @ColumnInfo(name = "timestamp")
    @Expose
    var savedTimestamp: Long = 0,

    @ColumnInfo(name = "expireTimestamp")
    @Expose
    var expireTimestamp: Long = 0
)

@ProvidedTypeConverter
class WaterfallConverter {
    @TypeConverter
    fun myWaterfallNameToTnt(value: WaterfallName) = value.ordinal

    @TypeConverter
    fun intToWaterfallNameEnum(value: Int) = value.toEnum<WaterfallName>()
}