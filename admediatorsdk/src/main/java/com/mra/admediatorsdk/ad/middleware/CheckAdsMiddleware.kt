package com.mra.admediatorsdk.ad.middleware

import com.mra.admediatorsdk.data.enums.WaterfallName

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */


/**
 * Base middleware class for check ad
 */
abstract class CheckAdsMiddleware {

    private var next: CheckAdsMiddleware? = null

    companion object {
        fun newInstance(middleware: CheckAdsMiddleware): CheckAdsMiddleware {
            var head = middleware
            head.next = middleware
            return middleware
        }
    }


    abstract fun check(position: Int = 0)

    protected open fun checkNext(position: Int) {
        next?.check(position)
    }

}