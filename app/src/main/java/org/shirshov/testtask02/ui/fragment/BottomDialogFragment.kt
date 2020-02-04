package org.shirshov.testtask02.ui.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import org.shirshov.testtask02.R
import org.shirshov.testtask02.databinding.BottomDialogFragmentBinding
import org.shirshov.testtask02.util.extension.embedFragment
import org.shirshov.testtask02.util.extension.screenHeightPx
import org.shirshov.testtask02.util.extension.toPixels

/**
 * Used instead of BottomSheetBehavior to get margin from top
 */
class BottomDialogFragment<T : BaseFragment> : DialogFragment(), DialogInterface.OnShowListener {

    private lateinit var b: BottomDialogFragmentBinding
    lateinit var content: T
    var onDone: ((T) -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        b = BottomDialogFragmentBinding.inflate(inflater)
        embedFragment(b.containerMain.id, content)
        return b.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            // activity here is null, so cannot easily evade static usage inside screenSizePx
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeightPx - 120.toPixels())
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.attributes.windowAnimations = R.style.DialogAnimationBottom
            it.setDimAmount(0.5f)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).also { it.setOnShowListener(this) }
    }

    override fun onShow(dialog: DialogInterface?) {
        // call onShow callback here if needed
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDone?.invoke(content)
    }


}