package org.shirshov.testtask.util

import android.util.Log
import org.shirshov.testtask.BuildConfig
import org.shirshov.testtask.util.LogUtil.CONTINUE_TAG
import org.shirshov.testtask.util.LogUtil.TAG
import org.shirshov.testtask.util.LogUtil.loggingEnabled
import java.net.UnknownHostException
import java.text.MessageFormat
import java.util.*

/**
 * This class wraps [android.util.Log] and provides following benefits:
 * - eliminates the need to manually add tag to each log method call
 * - allows to set logcat filter to show only [LogUtil] messages (by automatically adding [TAG] to all messages)
 * - shows clickable file name and line number at which log method was invoked
 * - allows output of messages more than 4000 symbols long (by splitting messages with [CONTINUE_TAG])
 * - allows to easy customize which logs are enabled and which disabled by various [loggingEnabled] flags
 * - fix for UnknownHostException silently ignored by default logger https://issuetracker.google.com/issues/36934828
 */

object LogUtil {

    private const val TAG = "LogUtilTag" // Filter logcat by this tag
    private const val MAX_LOG_ENTRY_SIZE = 3000
    private const val CONTINUE_TAG = "[CONTINUE]" // Long messages splitted by this tag

    var loggingEnabled: Boolean = BuildConfig.ISDEBUG
    var loggingEnabledErrors = true
        get() = field && loggingEnabled
    var loggingEnabledHttp = true
        get() = field && loggingEnabled
    var loggingEnabledWebSocket = true
        get() = field && loggingEnabled

    /**
     * List of class names that could appear between LogUtil entries in stackTrace and needs to be skipped. Only start of the class names is needed.
     */
    private var systemClasses = arrayOf("java", "com.android")

    /**
     * Wraps [android.util.Log.v] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun v(msg: Any) = helper(msg, { tag, message -> Log.v(tag, message) })

    /**
     * Wraps [android.util.Log.d] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun d(msg: Any) = helper(msg, { tag, message -> Log.d(tag, message) })

    /**
     * Wraps [android.util.Log.i] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun i(msg: Any) = helper(msg, { tag, message -> Log.i(tag, message) })

    /**
     * Wraps [android.util.Log.w] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun w(msg: Any) = helper(msg, { tag, message -> Log.w(tag, message) })

    /**
     * Wraps [android.util.Log.e] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun e(msg: Any) = helper(msg, { tag, message -> Log.e(tag, message) }, loggingEnabledErrors)

    /**
     * Wraps [android.util.Log.wtf] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun wtf(msg: Any) = helper(msg, { tag, message -> Log.wtf(tag, message) })

    /**
     * Additional method to easy excluding http from log output
     */
    fun http(msg: Any) = helper(msg, { tag, message -> Log.d(tag, message) }, loggingEnabledHttp)

    /**
     * Additional method to easy excluding web socket from log output
     */
    fun ws(msg: Any) = helper(msg, { tag, message -> Log.v(tag, message) }, loggingEnabledWebSocket)

    /**
     * Wraps [android.util.Log.v] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun v(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.v(tag, message) }, { tag, message, throwable -> Log.v(tag, message, throwable) })

    /**
     * Wraps [android.util.Log.d] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun d(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.d(tag, message) }, { tag, message, throwable -> Log.d(tag, message, throwable) })

    /**
     * Wraps [android.util.Log.i] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun i(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.i(tag, message) }, { tag, message, throwable -> Log.i(tag, message, throwable) })

    /**
     * Wraps [android.util.Log.w] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun w(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.w(tag, message) }, { tag, message, throwable -> Log.w(tag, message, throwable) })

    /**
     * Wraps [android.util.Log.e] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun e(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.e(tag, message) }, { tag, message, throwable -> Log.e(tag, message, throwable) },
        loggingEnabledErrors
    )

    /**
     * Wraps [android.util.Log.wtf] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun wtf(msg: Any, e: Throwable) = helper(msg, e, { tag, message -> Log.wtf(tag, message) }, { tag, message, throwable -> Log.wtf(tag, message, throwable) })

    private fun helper(
        msg: Any, e: Throwable, logAction: (String, String) -> Unit, logActionWithThrowable: (String, String, Throwable) -> Unit, enabled: Boolean? = null
    ) {
        if (enabled ?: loggingEnabled) {
            val m = if (e is UnknownHostException) { // fix for idiotic google decision https://issuetracker.google.com/issues/36934828
                msg.toString().plus("\n$e\n${e.printStackTrace()}")
            } else {
                msg
            }
            val messageParts = splitLongLogMessages(location + m)
            val iterator = messageParts.iterator()
            while (iterator.hasNext()) {
                val message = iterator.next()
                if (iterator.hasNext()) {
                    logAction(TAG, message)
                } else {
                    logActionWithThrowable(TAG, message, e)
                }
            }
        }
    }

    private fun helper(msg: Any, logAction: (String, String) -> Unit, enabled: Boolean? = null) {
        if (enabled ?: loggingEnabled) {
            for (message in splitLongLogMessages(location + msg)) {
                logAction(TAG, message)
            }
        }
    }

    private val location: String
        get() {
            var found = false
            for (traceElement in Thread.currentThread().stackTrace) {
                found = found || isTraceElementPointsToLogUtil(traceElement)
                if (found && !isTraceElementPointsToLogUtil(traceElement) && !isTraceElementPointsToSystemClass(traceElement)) {
                    return MessageFormat.format("({0}:{1}) ", traceElement.fileName, traceElement.lineNumber)
                }
            }
            return "[]: "
        }


    private fun isTraceElementPointsToLogUtil(trace: StackTraceElement): Boolean {
        return trace.className.startsWith(LogUtil::class.java.name)
    }

    private fun isTraceElementPointsToSystemClass(trace: StackTraceElement): Boolean {
        var result = false
        val traceClassName = trace.className
        for (systemClassName in systemClasses) {
            result = traceClassName.startsWith(systemClassName)
            if (result) {
                break
            }
        }
        return result
    }

    private fun splitLongLogMessages(logMessage: String): List<String> {
        var message = logMessage
        val splittedMessages = ArrayList<String>()
        while (true) {
            if (message.length > MAX_LOG_ENTRY_SIZE) {
                splittedMessages.add(message.substring(0, MAX_LOG_ENTRY_SIZE))
                message = CONTINUE_TAG + message.substring(MAX_LOG_ENTRY_SIZE, message.length)
            } else {
                splittedMessages.add(message)
                return splittedMessages
            }
        }
    }
}