package com.mra.admediatorsdk.core.base

import android.content.Context
import com.mra.admediatorsdk.R
import com.mra.admediatorsdk.global.GlobalFunction
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import retrofit2.Response

/**
 * Create by Mohammadreza Allahgholi
 *  Site: https://seniorandroid.ir
 */
abstract class BaseCoroutinesDataSource(private val mContext: Context) {

    private suspend fun <T> getResult(call: suspend () -> Response<T>): CustomResult<T> {
        try {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()
                return CustomResult.success(body)
            }

            return CustomResult.error(mContext.getString(R.string.server_error))
        } catch (e: Exception) {
            e.printStackTrace()
            return if (!GlobalFunction.instance.isNetworkAvailable)
                CustomResult.failed(
                    CustomResult.FailedMessage(
                        message = mContext.getString(R.string.error_network_connection),
                        code = CustomResult.ERROR_NETWORK
                    )
                )
            else
                CustomResult.failed(CustomResult.FailedMessage(e.message ?: e.toString()))
        }
    }

    protected fun <T> callApi(
        times: Int = 2, initialDelay: Long = 100, maxDelay: Long = 10000,
        factor: Double = 2.0, call: suspend () -> Response<T>
    ) = flow {

        var loopTimes = times
        var currentDelay = initialDelay
        emit(CustomResult.loading())
        loop@ while (loopTimes - 1 != 0) {

            loopTimes--
            val response = getResult(call)
            when (response.status) {
                CustomResult.Status.SUCCESS -> {
                    try {
                        emit(CustomResult.success(response.data))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    break@loop
                }

                CustomResult.Status.FAILD -> {
                    emit(response)
                    if (response.connectionError || response.failedMessage?.code?.toString() == "404") break@loop
                }

                CustomResult.Status.ERROR -> {
                    emit(response)
                    break@loop
                }

                else -> {}
            }

            delay(currentDelay)
            currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
        }

    }

}