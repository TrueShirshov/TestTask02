package org.shirshov.testtask.ui.navigation

import org.shirshov.testtask.R
import org.shirshov.testtask.ui.activity.mainActivity
import org.shirshov.testtask.ui.fragment.BaseFragment

object Navigate {

    private val fragmentManager get() = mainActivity.supportFragmentManager

    val currentFragment: BaseFragment
        get() = fragmentManager.findFragmentById(R.id.fragmentContainer) as BaseFragment

    fun back() {
        fragmentManager.popBackStack()
    }

    fun to(toFragment: BaseFragment, toBackStack: BaseFragment?) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (toBackStack != null) {
            fragmentTransaction.addToBackStack(toBackStack.javaClass.name)
        }
        fragmentTransaction.replace(mainActivity.b.fragmentContainer.id, toFragment)
        fragmentTransaction.commit()
    }

    private fun clearBackStack() {
        val fm = fragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }
}
