package com.mra.admediatorsdk.core.di


import com.mra.admediatorsdk.BuildConfig
import com.mra.admediatorsdk.data.database.createDataBaseInstance
import com.mra.admediatorsdk.data.remote.api.AdApiService
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  Dependency injection of methods required by the program
 *
 */
private val mGson = getGson()
val baseTapsellRetrofit: Retrofit = createBaseNetworkClient(gson = mGson,BuildConfig.TAPSELL_BASE_URL)
val adApiService: AdApiService = baseTapsellRetrofit.create(AdApiService::class.java)


val dataBaseModule = module {
    single { createDataBaseInstance(androidContext()) }
}

val gsonModule = module {
    single { mGson }
}

val networkModule = module {
    single { adApiService }
}

