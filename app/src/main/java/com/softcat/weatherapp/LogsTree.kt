package com.softcat.weatherapp

import android.annotation.SuppressLint
import android.util.Log
import com.softcat.data.BuildConfig
import timber.log.Timber

object LogsTree: Timber.Tree() {
    @SuppressLint("LogNotTimber")
    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
        val logTag: String = tag ?: DEFAULT_TAG
        val msg = if (t == null)
            message
        else if (message.isNotBlank())
            "$message: ${t.message}"
        else
            return

        when (priority) {
            Log.DEBUG -> if (BuildConfig.DEBUG) { Log.i(logTag, msg) }
            Log.INFO -> Log.i(logTag, msg)
            Log.ERROR -> Log.e(logTag, msg)
            Log.WARN -> Log.w(logTag, msg)
            Log.ASSERT -> Log.wtf(logTag, msg)
        }
        dumpToFile(logTag, msg)
    }

    private fun dumpToFile(tag: String, msg: String) {
        // dumping logic if needed
    }

    private val DEFAULT_TAG = LogsTree::class.simpleName.toString()
}