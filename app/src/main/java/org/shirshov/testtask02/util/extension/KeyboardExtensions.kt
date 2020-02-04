package org.shirshov.testtask02.util.extension

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentActivity
import org.shirshov.testtask02.ui.component.recycler.CoreRecycler

private fun Activity.getInputMethodManager(): InputMethodManager {
    return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
}

fun Activity.hideKeyboard(view: View) {
    getInputMethodManager().hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}

fun Activity.showKeyboard(view: View) {
    view.requestFocus()
    getInputMethodManager().showSoftInput(view, 0)
}

fun CoreRecycler.hideKeyboardOnScroll(activity: FragmentActivity?, focusHolder: View = this) {
    onScroll { focusHolder.findFocus()?.let { activity?.hideKeyboard(it) } }
}