package com.mra.admediatorsdk.global

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import org.koin.java.KoinJavaComponent

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
class GlobalFunction private constructor(){


    companion object {
        private var myInstance: GlobalFunction? = null

        val instance: GlobalFunction
            @Synchronized get() {
                if (myInstance == null) {
                    myInstance = GlobalFunction()
                }
                return myInstance!!
            }

        /**
         * If activity and service is running return true
         */

    }

    val isNetworkAvailable: Boolean
        get() {
            val mContext: Context by KoinJavaComponent.inject(Context::class.java)
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            var activeNetworkInfo: NetworkInfo? = null
            activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }

}