package com.mra.admediatorsdk.global.utils

import java.util.ArrayList

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
fun <T> List<T>.convertListToArrayList(): ArrayList<T> {
    return ArrayList(this)
}