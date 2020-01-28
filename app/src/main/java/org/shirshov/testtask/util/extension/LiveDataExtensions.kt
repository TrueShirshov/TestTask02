package org.shirshov.testtask.util.extension

import android.os.Looper
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun <T> mutableLiveData(default: T) = MutableLiveData<T>().apply { executeAndWaitOnUiIfNeeded { value = default } }

fun <T> MutableLiveData<T>.postNew(newValue: T) {
    if (value != newValue) {
        postValue(newValue)
    }
}

private fun executeAndWaitOnUiIfNeeded(block: () -> Unit) {
    if (isUiThread()) {
        block()
    } else {
        executeAndWaitOnUIThread(block)
    }
}

private fun executeAndWaitOnUIThread(block: () -> Unit) {
    runBlocking {
        MainScope().launch {
            block()
        }
    }
}

private fun isUiThread() = Looper.myLooper() == Looper.getMainLooper()

fun MutableLiveData<Boolean>.inverse() = (!value!!).also { value = it }

fun MutableLiveData<Boolean>.postInverse() = (!value!!).also { postValue(it) }