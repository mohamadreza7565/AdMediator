package com.mra.admediatorsdk.core.di

import com.mra.admediatorsdk.data.datasource.AdLocalDataSource
import com.mra.admediatorsdk.data.datasource.AdRemoteDataSource
import com.mra.admediatorsdk.data.repo.AdRepo
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  Dependency injection of repositories
 *
 */
val repositoryModule = module {
    single {
        AdRepo(
            localDataSource = AdLocalDataSource(roomAppDatabase = get()),
            remoteDataSource = AdRemoteDataSource(
                mContext = androidContext(),
                adApiService = get()
            )
        )
    }
}