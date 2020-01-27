package org.shirshov.testtask.util.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.widget.Toast
import androidx.core.content.ContextCompat

@SuppressLint("StaticFieldLeak") // It's ok to hold baseContext
object Ui {
    lateinit var baseContext: Context

    fun showToast(stringId: Int) {
        showToast(stringId.toStringRes())
    }

    fun showToast(text: String) {
        Toast.makeText(baseContext, text, Toast.LENGTH_SHORT).show()
    }
}

fun Int.toPixels(): Int {
    return this * Resources.getSystem().displayMetrics.density.toInt()
}

fun Double.toPixels(): Double {
    return this * Resources.getSystem().displayMetrics.density
}

fun Int.toColorRes(): Int {
    return ContextCompat.getColor(Ui.baseContext, this)
}

fun Int.toStringRes(vararg params: Any): String {
    return Ui.baseContext.getString(this, *params)
}