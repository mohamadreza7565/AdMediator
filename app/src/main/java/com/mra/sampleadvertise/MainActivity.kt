package com.mra.sampleadvertise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.mra.admediatorsdk.core.middleware.AdMediator
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd

class MainActivity : AppCompatActivity(), IAdMediatorRequestAdListener, IAdMediatorRequestShowAd {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AdMediator.initialize()

        findViewById<Button>(R.id.btnRequest).setOnClickListener {
            AdMediator.requestAd(this)
        }


    }

    override fun onRequestAdStart() {
        Log.i(TAG, "Request ad started")
    }

    override fun onRequestAdResponse(adId: String, zoneId: String, waterfallName: WaterfallName) {
        Log.i(TAG, "Response available ad")
        AdMediator.showAd(this, adId, zoneId, waterfallName, this)
    }

    override fun onRequestAdFailure(errorMessage: String?) {
        Log.e(TAG, "Error get available ad -> $errorMessage")
    }

    override fun onShowAdStart() {
        Log.i(TAG, "Show ad started")
    }

    override fun onShowAdFailure(errorMessage: String?) {
        Log.e(TAG, "Error show ad -> $errorMessage")
    }

    override fun onShowAdClick(message: String?) {
        Log.i(TAG, "Click ad")
    }

    override fun onShowAdComplete(message: String?) {
        Log.i(TAG, "Complete show ad -> $message")
    }

    override fun onShowAdClosed(message: String?) {
        Log.i(TAG, "Ad closed -> $message")
    }
}