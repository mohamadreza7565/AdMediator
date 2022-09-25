package com.mra.admediatorsdk.interfaces

import com.mra.admediatorsdk.data.model.AdNetwork
import com.mra.admediatorsdk.data.model.Waterfall

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
interface IAdMediatorInitializer {

    fun onInitializeStart()

    fun onInitializeResponse()

    fun onInitializeFailure(errorMessage: String?)

}