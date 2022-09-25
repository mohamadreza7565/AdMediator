package com.mra.admediatorsdk.interfaces

import com.mra.admediatorsdk.data.model.AdNetwork
import com.mra.admediatorsdk.data.model.Waterfall

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
interface IAdMediatorRequestShowAd {

    fun onShowAdStart()

    fun onShowAdFailure(errorMessage: String?)

    fun onShowAdClick(message:String?)

    fun onShowAdComplete(message:String?)

    fun onShowAdClosed(message:String?)

}