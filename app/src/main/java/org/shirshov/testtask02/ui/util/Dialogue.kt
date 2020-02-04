package org.shirshov.testtask02.ui.util

import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.shirshov.testtask02.R
import org.shirshov.testtask02.ui.activity.mainActivity
import org.shirshov.testtask02.util.extension.toColorRes
import org.shirshov.testtask02.util.extension.toStringRes

@Suppress("MemberVisibilityCanBePrivate")
object Dialogue {
    private const val CENTER_BUTTONS = true
    private const val POSITIVE_BUTTON_WEIGHT = 10
    private const val NEGATIVE_BUTTON_WEIGHT = 20

    private fun create(c: Context, title: String, message: String, buttonOk: String, buttonCancel: String?, listener: DialogInterface.OnClickListener): AlertDialog {
        return create(c, title, message, buttonOk, buttonCancel, listener, null)
    }

    private fun create(
        c: Context, title: String, message: String, buttonOk: String, buttonCancel: String?,
        listener: DialogInterface.OnClickListener, additionalView: View?
    ): AlertDialog {
        val builder = AlertDialog.Builder(c, R.style.CustomDialogTheme)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(buttonOk, listener)
        if (buttonCancel != null && buttonCancel.isNotEmpty()) {
            builder.setNegativeButton(buttonCancel, listener)
        }
        if (additionalView != null) {
            builder.setView(additionalView)
        }
        builder.setCancelable(false)
        return builder.create()
    }

    private fun convertListener(actionOk: (() -> Unit)?, actionCancel: (() -> Unit)?) = DialogInterface.OnClickListener { _, buttonId ->
        if (buttonId == DialogInterface.BUTTON_POSITIVE && actionOk != null) {
            actionOk()
        }
        if (buttonId == DialogInterface.BUTTON_NEGATIVE && actionCancel != null) {
            actionCancel()
        }
    }

    private fun customizeOneButton(dialog: AlertDialog) {
        val btnPositive = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        @Suppress("ConstantConditionIf")
        if (CENTER_BUTTONS) {
            val params = btnPositive.layoutParams as LinearLayout.LayoutParams
            params.setMargins((btnPositive.paddingLeft * -2.5).toInt(), 0, 0, 0)
        }
        customizeButton(btnPositive, POSITIVE_BUTTON_WEIGHT)
    }

    fun customizeTwoButtons(dialog: AlertDialog) {
        val btnPositive = dialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE)
        val btnNegative = dialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE)
        customizeButton(btnPositive, POSITIVE_BUTTON_WEIGHT)
        customizeButton(btnNegative, NEGATIVE_BUTTON_WEIGHT)
    }

    private fun customizeButton(button: Button?, weight: Int) {
        if (button == null) {
            return
        }
        @Suppress("ConstantConditionIf")
        if (CENTER_BUTTONS) {
            val layoutParams = button.layoutParams as LinearLayout.LayoutParams
            layoutParams.weight = weight.toFloat()
            button.layoutParams = layoutParams
        }
        colorizeButton(button)
    }

    private fun colorizeButton(button: Button?) {
        button?.setTextColor(R.color.colorAccent.toColorRes())
    }

    // PUBLIC METHODS FROM HERE

    fun show(title: String, message: String, buttonOk: String, listener: DialogInterface.OnClickListener) {
        MainScope().launch {
            val dialog = create(mainActivity, title, message, buttonOk, null, listener)
            dialog.show()
            customizeOneButton(dialog)
        }
    }

    fun showTwoButtons(title: String, message: String, buttonOk: String, buttonCancel: String, ok: () -> Unit, cancel: () -> Unit) {
        MainScope().launch {
            val dialog = create(mainActivity, title, message, buttonOk, buttonCancel, convertListener(ok, cancel))
            dialog.show()
            customizeTwoButtons(dialog)
        }
    }

    fun showTwoButtons(@StringRes title: Int, @StringRes message: Int, @StringRes buttonOk: Int, @StringRes buttonCancel: Int, ok: () -> Unit, cancel: () -> Unit) {
        showTwoButtons(title.toStringRes(), message.toStringRes(), buttonOk.toStringRes(), buttonCancel.toStringRes(), ok, cancel)
    }

    fun showTwoButtons(title: String, @StringRes message: Int, @StringRes buttonOk: Int, @StringRes buttonCancel: Int, ok: () -> Unit, cancel: () -> Unit) {
        showTwoButtons(title, message.toStringRes(), buttonOk.toStringRes(), buttonCancel.toStringRes(), ok, cancel)
    }

    fun showTwoButtons(@StringRes title: Int, message: String, @StringRes buttonOk: Int, @StringRes buttonCancel: Int, ok: () -> Unit, cancel: () -> Unit) {
        showTwoButtons(title.toStringRes(), message, buttonOk.toStringRes(), buttonCancel.toStringRes(), ok, cancel)
    }

    fun showSimplest(title: String, message: String, listener: DialogInterface.OnClickListener) {
        show(title, message, mainActivity.getString(R.string.btn_ok), listener)
    }

    fun showSimpleImage(title: String, message: String, actionOk: () -> Unit, actionCancel: () -> Unit, additionalView: View) {
        val c = mainActivity
        val dialog = create(c, title, message, c.getString(R.string.btn_ok), c.getString(R.string.btn_cancel), convertListener(actionOk, actionCancel), additionalView)
        dialog.show()
        colorizeButton(dialog.getButton(DialogInterface.BUTTON_POSITIVE))
        colorizeButton(dialog.getButton(DialogInterface.BUTTON_NEGATIVE))
    }

    fun show(@StringRes text: Int) {
        show("", text.toStringRes(), null)
    }

    fun show(@StringRes text: Int, action: () -> Unit) {
        show("", text.toStringRes(), action)
    }

    fun show(text: String) {
        show("", text, null)
    }

    fun show(text: String, action: () -> Unit) {
        show("", text, action)
    }

    fun show(@StringRes title: Int, @StringRes text: Int) {
        show(title, text, null)
    }

    fun show(@StringRes titleId: Int, @StringRes textId: Int, action: (() -> Unit)?) {
        show(titleId.toStringRes(), textId.toStringRes(), action)
    }

    fun show(@StringRes titleId: Int, text: String, action: (() -> Unit)?) {
        show(titleId.toStringRes(), text, action)
    }

    fun show(title: String, @StringRes textId: Int, action: (() -> Unit)?) {
        show(title, textId.toStringRes(), action)
    }

    fun show(title: String, text: String, action: (() -> Unit)?) {
        showSimplest(title, text, convertListener(action, null))
    }

    fun showError(error: String) {
        show(R.string.dialog_error_title, error, null)
    }

    fun showError(error: String, action: () -> Unit) {
        show(R.string.dialog_error_title, error, action)
    }

    fun showError(@StringRes errorId: Int) {
        showError(errorId.toStringRes())
    }

    fun showError(@StringRes errorId: Int, action: () -> Unit) {
        showError(errorId.toStringRes(), action)
    }
}
