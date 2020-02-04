package org.shirshov.testtask02.ui.fragment

import androidx.annotation.MainThread
import androidx.lifecycle.*
import kotlinx.coroutines.*
import org.shirshov.testtask02.util.LogUtil
import org.shirshov.testtask02.util.extension.mutableLiveData
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext


abstract class BaseViewModel : ViewModel(), LifecycleObserver, CoroutineScope {

    val posting = mutableLiveData(false)
    val loading = mutableLiveData(false)

    private var posts = JobLauncher(posting, this)
        set(value) {
            posts.cancel()
            field = value
        }

    private var loads = JobLauncher(loading, this)
        set(value) {
            loads.cancel()
            field = value
        }

    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

    @MainThread
    fun reload(force: Boolean = false) {
        if (force || !loads.isActive) {
            loads = JobLauncher(loading, this)
            loadData()
        }
    }

    protected abstract fun loadData()

    protected fun load(block: suspend CoroutineScope.() -> Unit) = loads.add(block)

    protected fun post(block: suspend CoroutineScope.() -> Unit) = posts.add(block)

    protected open fun cancel() {
        loads.cancel()
        loads = JobLauncher(loading, this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onResume() = reload()

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    private fun onStop() = cancel()

    // Mirrors state of launched jobs to loadingState
    private class JobLauncher(private val loadingState: MutableLiveData<Boolean>, parentScope: CoroutineScope) {

        private val root = Job()
        private val scope: CoroutineScope

        init {
            scope = parentScope + root
        }

        val isActive: Boolean
            get() = activeCount > 0

        fun cancel() {
            root.cancel()
        }

        fun add(block: suspend CoroutineScope.() -> Unit) {
            scope.launch {
                activeCount++
                try {
                    block()
                } catch (e: CancellationException) {
                    LogUtil.w("Job was cancelled", e)
                } catch (e: Exception) {
                    LogUtil.e("Thread crashed", e)
                } finally {
                    activeCount--
                }
            }
        }

        private var activeCount = 0
            set(value) {
                if (field == 0 && value > 0) {
                    loadingState.postValue(true)
                } else if (field > 0 && value == 0) {
                    loadingState.postValue(false)
                }
                field = value
            }
    }

}