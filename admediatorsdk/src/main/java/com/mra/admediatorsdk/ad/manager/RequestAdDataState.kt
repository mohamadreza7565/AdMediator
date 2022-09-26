package com.mra.admediatorsdk.ad.manager

sealed class RequestAdDataState<out R> {
    data class AvailableAd<out T : RequestAdModel>(val data: T) :
        RequestAdDataState<T>()

    data class NotAvailableAd(val messageError: String?) : RequestAdDataState<Nothing>()
}