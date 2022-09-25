package com.mra.admediatorsdk.core.middleware

import android.app.Activity
import android.content.Context
import android.util.Log
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall
import com.mra.admediatorsdk.data.repo.AdRepo
import com.mra.admediatorsdk.global.utils.convertListToArrayList
import com.mra.admediatorsdk.interfaces.IAdMediatorInitializer
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd
import com.unity3d.ads.IUnityAdsShowListener
import com.unity3d.ads.UnityAds
import ir.tapsell.sdk.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 *
 *  The request to the server to receive advertisements and check its availability and its final display is done in this class
 *
 */
class ServerAdsMiddleware {

    private var middleware: CheckAdsMiddleware? = null
    private val mAdRepo: AdRepo by KoinJavaComponent.inject(AdRepo::class.java)
    private val mContext: Context by KoinJavaComponent.inject(Context::class.java)


    /**
     * Set waterfalls into middleware for check availability
     */
    private fun setMiddleware(waterfalls: ArrayList<Waterfall>) {
        val middleware = CheckAdsMiddleware.newInstance(
            AdsAvailableMiddleware(getAvailableWaterfalls(waterfalls)),
        )
        this.middleware = middleware
    }


    /**
     * Get list of available waterfalls in adNetwork
     */
    private fun getAvailableWaterfalls(waterfalls: ArrayList<Waterfall>): ArrayList<Waterfall> {
        val availableWaterfalls: ArrayList<Waterfall> = ArrayList()
        waterfalls.forEach { _waterfall ->
            AdMediator.adNetworks.find { it.name.value == _waterfall.name }?.let {
                availableWaterfalls.add(_waterfall)
            }
        }
        return availableWaterfalls
    }


    /**
     * Get list of adNetworks from api
     */
    fun getAdNetworks(listener: IAdMediatorInitializer? = null) {
        CoroutineScope(Dispatchers.IO).launch {
            mAdRepo.getAdNetworks().collect {
                when (it.status) {
                    CustomResult.Status.LOADING -> listener?.onInitializeStart()
                    CustomResult.Status.SUCCESS -> {
                        it.data?.adNetworks?.let {
                            AdMediator.adNetworks = it
                            listener?.onInitializeResponse()
                        }
                    }
                    CustomResult.Status.ERROR -> {
                        Log.e(
                            "AdMediator",
                            "requestAd -> ${it.errorMessage} "
                        )
                        listener?.onInitializeFailure(it.errorMessage)
                    }

                }
            }
        }
    }

    /**
     * Check list of saved waterfalls into DB is expired or not
     *
     * If all of the saved waterfalls is expired get waterfalls from api and replace they
     *
     */
    fun requestAd(listener: IAdMediatorRequestAdListener) {

        val availableDbWaterfall = mAdRepo.getAvailableRewardedWaterfall()
        if (availableDbWaterfall.isNotEmpty())
            checkAvailableDbWaterfall(availableDbWaterfall.convertListToArrayList(), listener)
        else {
            getRewardedWaterfalls(listener)
        }

    }

    private fun checkAvailableDbWaterfall(
        availableDbWaterfall: ArrayList<Waterfall>,
        listener: IAdMediatorRequestAdListener
    ) {
        setMiddleware(availableDbWaterfall)
        middleware?.check(
            onAvailableAd = { adId, zoneId, waterfallName ->
                listener.onRequestAdStart()
                listener.onRequestAdResponse(adId, zoneId, waterfallName)
            },
            onNotAvailableAd = {
                getRewardedWaterfalls(listener)
            }
        )
    }


    /**
     * Get rewarded waterfalls from api
     */
    private fun getRewardedWaterfalls(listener: IAdMediatorRequestAdListener) {
        CoroutineScope(Dispatchers.IO).launch {
            mAdRepo.getRewardedWaterfall().collect {
                when (it.status) {
                    CustomResult.Status.LOADING -> listener.onRequestAdStart()
                    CustomResult.Status.SUCCESS -> {
                        it.data?.waterfalls?.let {
                            setMiddleware(it)
                            middleware?.check(
                                onAvailableAd = { adId, zoneId, waterfallName ->
                                    listener.onRequestAdResponse(adId, zoneId, waterfallName)
                                },
                                onNotAvailableAd = {
                                    listener.onRequestAdFailure("Not any ad available")
                                }
                            )
                        }
                    }
                    else -> listener.onRequestAdFailure(it.errorMessage)
                }
            }
        }
    }


    /**
     * Check waterfall ad type and show it
     */
    fun showAd(
        mActivity: Activity,
        adId: String,
        zoneId: String,
        waterfallName: WaterfallName,
        listenerShowAd: IAdMediatorRequestShowAd
    ) {
        when (waterfallName) {
            WaterfallName.TAPSELL -> showTapsellAds(adId, zoneId, listenerShowAd)
            WaterfallName.UNIT_ADS -> UnityAds.show(
                mActivity,
                adId,
                showUnityListener(listenerShowAd)
            )
            else -> {
                listenerShowAd.onShowAdFailure("Waterfall not supported")
            }
        }
    }


    /**
     * Show tapsell ad
     */
    private fun showTapsellAds(
        adId: String,
        zoneId: String,
        listenerShowAd: IAdMediatorRequestShowAd
    ) {

        Tapsell.showAd(
            mContext,
            zoneId,
            adId,
            TapsellShowOptions(),
            showTapsellListener(listenerShowAd, zoneId)
        )
    }

    private fun showTapsellListener(
        listenerShowAd: IAdMediatorRequestShowAd,
        zoneId: String
    ) = object : TapsellAdShowListener() {
        override fun onOpened() {
            listenerShowAd.onOpenAd()
        }

        override fun onClosed() {
            listenerShowAd.onShowAdClosed(zoneId)
        }

        override fun onError(message: String) {
            listenerShowAd.onShowAdFailure(message)
        }

        override fun onRewarded(completed: Boolean) {
            listenerShowAd.onShowAdComplete("Completed")
        }
    }


    private fun showUnityListener(listenerShowAd: IAdMediatorRequestShowAd): IUnityAdsShowListener =
        object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                p0: String?,
                p1: UnityAds.UnityAdsShowError?,
                p2: String?
            ) {
                listenerShowAd.onShowAdFailure(p0)
            }

            override fun onUnityAdsShowStart(p0: String?) {
                listenerShowAd.onShowAdStart()
            }

            override fun onUnityAdsShowClick(p0: String?) {
                listenerShowAd.onShowAdClick(p0)
            }

            override fun onUnityAdsShowComplete(
                p0: String?,
                p1: UnityAds.UnityAdsShowCompletionState?
            ) {
                listenerShowAd.onShowAdComplete(p0)
            }

        }


}