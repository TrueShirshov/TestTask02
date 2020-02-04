package org.shirshov.testtask02.util.extension

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.shirshov.testtask02.ui.activity.mainActivity
import org.shirshov.testtask02.ui.fragment.BaseFragment

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

val screenWidthPx: Int
    get() {
        val metrics = DisplayMetrics()
        mainActivity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.widthPixels
    }

val screenHeightPx: Int
    get() {
        val metrics = DisplayMetrics()
        mainActivity.windowManager.defaultDisplay.getMetrics(metrics)
        return metrics.heightPixels
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

fun Context.initAttributes(attrs: AttributeSet?, styleable: IntArray, block: TypedArray.() -> Unit) {
    val typedArray = obtainStyledAttributes(attrs, styleable)
    block(typedArray)
    typedArray.recycle()
}

fun Fragment.embedFragment(containerId: Int, fragment: BaseFragment) {
    childFragmentManager.beginTransaction().replace(containerId, fragment).commit()
}