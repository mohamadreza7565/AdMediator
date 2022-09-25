package com.mra.admediatorsdk.core.middleware

import android.app.Activity
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.AdNetwork
import com.mra.admediatorsdk.interfaces.IAdMediatorInitializer
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd


/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  Requests related to receiving and displaying advertisements are made in this class
 *
 */
object AdMediator {

    lateinit var adNetworks: MutableList<AdNetwork>
    private val server = ServerAdsMiddleware()

    /**
     * Get adNetworks from server
     * and
     * initialize they
     */
    fun initialize(listener: IAdMediatorInitializer? = null) {
        server.getAdNetworks(listener)
    }

    /**
     * If AdMediator is initialize return true
     */
    fun isInitialized() = AdMediator::adNetworks.isInitialized


    /**
     * Get waterfalls from server or database
     *  and
     *  return available ad
     */
    fun requestAd(listener: IAdMediatorRequestAdListener) {
        if (isInitialized()) {
            server.requestAd(listener)
        } else {
            listener.onRequestAdFailure("Ad mediator not initialized")
        }

    }


    /**
     * Show advertise
     */
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