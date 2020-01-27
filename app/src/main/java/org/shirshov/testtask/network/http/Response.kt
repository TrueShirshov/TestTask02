package org.shirshov.testtask.network.http

import org.shirshov.testtask.R
import org.shirshov.testtask.ui.util.Dialogue
import org.shirshov.testtask.util.LogUtil
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class UnprocessedResponse<T>(private val response: Response<T>) {

    fun onDone(onSuccess: (T) -> Unit) {
        onDone(onSuccess) { false }
    }

    fun onDone(onSuccess: (T) -> Unit, onFail: (Exception?) -> Boolean) {
        if (response.success) {
            onSuccess(response.result!!)
        } else {
            if (!onFail(response.error)) {
                when (val e = response.error) {
                    is UnknownHostException -> Dialogue.showError(R.string.dialog_error_text_no_connection)
                    is ConnectException,
                    is SocketTimeoutException -> Dialogue.showError(R.string.dialog_error_text_timeout)
                    else -> {
                        e?.let { LogUtil.e(e, e) }
                        Dialogue.showError(R.string.dialog_error_text_unexpected)
                    }
                }
            }
        }
    }

}

class Response<T>(val result: T?, val error: Exception?) {
    val success: Boolean get() = result != null && error == null
}

data class ResponseContainer<T>(
    val data: T
)