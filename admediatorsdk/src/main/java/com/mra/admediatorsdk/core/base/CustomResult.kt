package com.mra.admediatorsdk.core.base

data class CustomResult<out T>(
    val status: Status,
    val data: T? = null,
    val errorMessage: String? = null,
    val failedMessage: FailedMessage? = null,
    val connectionError: Boolean = false
) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING,
        FAILD
    }

    data class FailedMessage(
        var message: String? = null,
        var code: Int? = -1,
        var errorBody: String? = null
    )

    companion object {
        fun <T> success(data: T?): CustomResult<T> {
            return CustomResult(Status.SUCCESS, data, null)
        }

        fun <T> error(errorModel: String): CustomResult<T> {
            return CustomResult(status = Status.ERROR, errorMessage = errorModel)
        }

        fun <T> failed(
            failedMessage: FailedMessage? = null,
            connectionError: Boolean = false
        ):
                CustomResult<T> {
            return CustomResult(
                status = Status.FAILD, failedMessage = failedMessage,
                connectionError = connectionError
            )
        }

        fun <T> loading(data: T? = null): CustomResult<T> {
            return CustomResult(Status.LOADING, data, null)
        }

        const val ERROR_NETWORK = 1001
    }
}