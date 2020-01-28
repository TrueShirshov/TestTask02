package org.shirshov.testtask.util.extension

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.SearchView
import org.shirshov.testtask.R

fun SearchView.noSearchIcon(): SearchView {
    setIconifiedByDefault(false)

    // Reduce huge left margin for no icon case
    val searchEditFrame = findViewById<LinearLayout>(R.id.search_edit_frame)
    (searchEditFrame.layoutParams as ViewGroup.MarginLayoutParams).leftMargin = (-8).toPixels()

    // Remove search icon
    val searchIcon = findViewById<AppCompatImageView>(R.id.search_mag_icon)
    searchIcon.layoutParams = LinearLayout.LayoutParams(0, 0)

    return this
}

val SearchView.innerTextView: EditText
    get() = findViewById(R.id.search_src_text)

fun SearchView.queryHint(hint: String): SearchView {
    queryHint = hint
    return this
}

fun SearchView.hintTextColor(color: Int): SearchView {
    innerTextView.setHintTextColor(color)
    return this
}

fun SearchView.textColor(color: Int): SearchView {
    innerTextView.setTextColor(color)
    return this
}

fun SearchView.highlightColor(color: Int): SearchView {
    innerTextView.highlightColor = color
    return this
}

fun SearchView.closeIcon(icon: Drawable?): SearchView {
    val closeIcon = findViewById<AppCompatImageView>(R.id.search_close_btn)
    icon?.let { closeIcon.setImageDrawable(it) }
    return this
}

fun SearchView.setOnSearchAction(action: () -> Unit) {
    imeOptions = EditorInfo.IME_ACTION_SEARCH
    innerTextView.setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            action()
        }
        false
    }
}