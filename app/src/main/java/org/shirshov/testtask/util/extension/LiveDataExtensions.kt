package org.shirshov.testtask.util.extension

import androidx.lifecycle.MutableLiveData


fun <T> mutableLiveData(default: T) = MutableLiveData<T>().apply { postValue(default) }

fun <T> MutableLiveData<T>.postNew(newValue: T) {
    if (value != newValue) {
        postValue(newValue)
    }
}

fun MutableLiveData<Boolean>.inverse() = (!value!!).also { value = it }

fun MutableLiveData<Boolean>.postInverse() = (!value!!).also { postValue(it) }