package com.mra.admediatorsdk.core.middleware

import android.app.Activity
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.AdNetwork
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd



/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
object AdMediator {

    lateinit var adNetworks: MutableList<AdNetwork>
    private val server = ServerAdsMiddleware()

    fun initialize() {
        server.getAdNetworks()
    }


    fun isInitialized() = AdMediator::adNetworks.isInitialized

    fun requestAd(listener: IAdMediatorRequestAdListener) {
        if (isInitialized()) {
            server.requestAd(listener)
        } else {
            listener.onRequestAdFailure("Ad mediator not initialized")
        }

    }

    fun showAd(
        maActivity: Activity,
        adId: String,
        zoneId: String,
        waterfallName: WaterfallName,
        listenerShowAd: IAdMediatorRequestShowAd
    ) {
        if (isInitialized()) {
            server.showAd(maActivity, adId, zoneId, waterfallName, listenerShowAd)
        } else {
            listenerShowAd.onShowAdFailure("Ad mediator not initialized")
        }
    }


}