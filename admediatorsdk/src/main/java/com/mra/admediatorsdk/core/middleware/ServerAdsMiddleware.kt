package com.mra.admediatorsdk.core.middleware

import android.app.Activity
import android.content.Context
import android.util.Log
import com.mra.admediatorsdk.core.base.CustomResult
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.data.model.Waterfall
import com.mra.admediatorsdk.data.repo.AdRepo
import com.mra.admediatorsdk.global.utils.convertListToArrayList
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd
import com.unity3d.ads.IUnityAdsLoadListener
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
 */
class ServerAdsMiddleware {

    private var middleware: CheckAdsMiddleware? = null
    private val mAdRepo: AdRepo by KoinJavaComponent.inject(AdRepo::class.java)
    private val mContext: Context by KoinJavaComponent.inject(Context::class.java)

    private fun setMiddleware(waterfalls: ArrayList<Waterfall>) {
        val middleware = CheckAdsMiddleware.newInstance(
            AdsAvailableMiddleware(getAvailableWaterfalls(waterfalls)),
        )
        this.middleware = middleware
    }

    private fun getAvailableWaterfalls(waterfalls: ArrayList<Waterfall>): ArrayList<Waterfall> {
        val availableWaterfalls: ArrayList<Waterfall> = ArrayList()
        waterfalls.forEach { _waterfall ->
            AdMediator.adNetworks.find { it.name.value == _waterfall.name }?.let {
                availableWaterfalls.add(_waterfall)
            }
        }
        return availableWaterfalls
    }

    fun getAdNetworks() {
        CoroutineScope(Dispatchers.IO).launch {
            mAdRepo.getAdNetworks().collect {
                when (it.status) {
                    CustomResult.Status.SUCCESS -> {
                        it.data?.adNetworks?.let {
                            AdMediator.adNetworks = it
                        }
                    }
                    CustomResult.Status.ERROR -> Log.e(
                        "AdMediator",
                        "requestAd -> ${it.errorMessage} "
                    )
                }
            }
        }
    }

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
                listener.onRequestAdResponse(adId, zoneId, waterfallName)
            },
            onNotAvailableAd = {
                getRewardedWaterfalls(listener)
            }
        )
    }

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
                showListener(listenerShowAd)
            )
            else -> {
                listenerShowAd.onShowAdFailure("Waterfall not supported")
            }
        }
    }


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
            object : TapsellAdShowListener() {
                override fun onOpened() {

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
            })
    }

    private fun requestUnityAds(
        mActivity: Activity,
        zoneId: String,
        listenerShowAd: IAdMediatorRequestShowAd
    ) {

        if (UnityAds.isInitialized())
            UnityAds.load(zoneId, iUnityAdsLoadListener(mActivity, listenerShowAd))
        else
            listenerShowAd.onShowAdFailure("Unity ads not initialized")


    }

    private fun iUnityAdsLoadListener(
        mActivity: Activity,
        listenerShowAd: IAdMediatorRequestShowAd
    ) =
        object : IUnityAdsLoadListener {
            override fun onUnityAdsAdLoaded(p0: String?) {
                UnityAds.show(mActivity, p0, showListener(listenerShowAd))
            }

            override fun onUnityAdsFailedToLoad(
                p0: String?,
                p1: UnityAds.UnityAdsLoadError?,
                p2: String?
            ) {
                listenerShowAd.onShowAdFailure(p0)
            }

        }

    private fun showListener(listenerShowAd: IAdMediatorRequestShowAd): IUnityAdsShowListener =
        object : IUnityAdsShowListener {
            override fun onUnityAdsShowFailure(
                p0: String?,
                p1: UnityAds.UnityAdsShowError?,
                p2: String?
            ) {
                listenerShowAd.onShowAdFailure(p0)
            }

            override fun onUnityAdsShowStart(p0: String?) {

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