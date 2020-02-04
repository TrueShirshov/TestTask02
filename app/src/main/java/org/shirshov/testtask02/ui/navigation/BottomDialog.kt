package org.shirshov.testtask02.ui.navigation

import androidx.fragment.app.Fragment
import org.shirshov.testtask02.ui.fragment.BaseFragment
import org.shirshov.testtask02.ui.fragment.BottomDialogFragment

object BottomDialog {

    // Allows only one dialog per presenter at the same time (e.g. in case of fast clicking)
    private val activePresenters = mutableSetOf<Fragment>()

    // Be careful: onPause for content called after onDismiss
    fun <T : BaseFragment> show(presenter: BaseFragment, content: T, onDismiss: (T) -> Unit) {
        if (activePresenters.contains(presenter)) return
        activePresenters.add(presenter)
        val bottomDialog = BottomDialogFragment<T>()
        bottomDialog.onDone = {
            onDismiss(it)
            activePresenters.remove(presenter)
        }
        bottomDialog.content = content
        presenter.activity?.let { bottomDialog.show(it.supportFragmentManager, bottomDialog.tag) }
    }
}
