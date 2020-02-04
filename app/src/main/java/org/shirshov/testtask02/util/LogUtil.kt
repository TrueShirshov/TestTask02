package org.shirshov.testtask02.util

import android.util.Log
import org.shirshov.testtask02.BuildConfig
import org.shirshov.testtask02.util.LogUtil.CONTINUE_TAG
import org.shirshov.testtask02.util.LogUtil.TAG
import org.shirshov.testtask02.util.LogUtil.loggingEnabled
import java.io.PrintWriter
import java.io.StringWriter
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
    fun v(msg: Any, e: Throwable? = null) = helper(msg, Log::v, e)

    /**
     * Wraps [android.util.Log.d] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun d(msg: Any, e: Throwable? = null) = helper(msg, Log::d, e)

    /**
     * Wraps [android.util.Log.i] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun i(msg: Any, e: Throwable? = null) = helper(msg, Log::i, e)

    /**
     * Wraps [android.util.Log.w] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun w(msg: Any, e: Throwable? = null) = helper(msg, Log::w, e)

    /**
     * Wraps [android.util.Log.e] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun e(msg: Any, e: Throwable? = null) = helper(msg, Log::e, e, loggingEnabledErrors)

    /**
     * Wraps [android.util.Log.wtf] as stated in class description
     *
     * @param msg Message to be printed in log
     */
    fun wtf(msg: Any, e: Throwable? = null) = helper(msg, Log::wtf, e)

    /**
     * Additional method to easy excluding http from log output
     */
    fun http(msg: Any, e: Throwable? = null) = helper(msg, Log::d, e, loggingEnabledHttp)

    /**
     * Additional method to easy excluding web socket from log output
     */
    fun ws(msg: Any, e: Throwable? = null) = helper(msg, Log::v, e, loggingEnabledWebSocket)

    private fun helper(msg: Any, logAction: (String, String) -> Unit, e: Throwable? = null, enabled: Boolean? = null) {
        if (enabled ?: loggingEnabled) {
            val fullMessage = if (e == null) {
                "$location $msg"
            } else {
                val sw = StringWriter()
                val pw = PrintWriter(sw)
                e.printStackTrace(pw)
                pw.flush()
                "$location $msg\n$sw" // do not use Log.getStackTraceString(Throwable) https://issuetracker.google.com/issues/36934828
            }
            for (message in splitLongLogMessages(fullMessage)) {
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