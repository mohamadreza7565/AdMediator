package com.mra.sampleadvertise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import com.mra.admediatorsdk.ad.AdMediator
import com.mra.admediatorsdk.data.enums.WaterfallName
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestAdListener
import com.mra.admediatorsdk.interfaces.IAdMediatorRequestShowAd

class MainActivity : AppCompatActivity(), IAdMediatorRequestAdListener, IAdMediatorRequestShowAd {

    private val TAG = "MainActivity"
    private lateinit var pbLoading: ProgressBar
    private lateinit var btnRequest: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }

    private fun init() {
        AdMediator.initialize()
        initView()
        initClick()
    }

    private fun initClick() {
        btnRequest.setOnClickListener {
            AdMediator.requestAd(this)
        }
    }

    private fun initView() {
        pbLoading = findViewById(R.id.pbLoading)
        btnRequest = findViewById(R.id.btnRequest)
    }

    override fun onRequestAdStart() {
        pbLoading.visibility = View.VISIBLE
        Log.i(TAG, "Request ad started")
    }

    override fun onRequestAdResponse(adId: String?, zoneId: String, waterfallName: WaterfallName) {
        Log.i(TAG, "Response available ad")
        pbLoading.visibility = View.GONE
        adId?.let {
            AdMediator.showAd(this, adId, zoneId, waterfallName, this)
        }
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

    override fun onOpenAd() {
        Log.i(TAG, "Open ad")

    }

    override fun onShowAdComplete(message: String?) {
        Log.i(TAG, "Complete show ad -> $message")
    }

    override fun onShowAdClosed(message: String?) {
        Log.i(TAG, "Ad closed -> $message")
    }
}