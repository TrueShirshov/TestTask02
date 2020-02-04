package org.shirshov.testtask02.ui.navigation

import org.shirshov.testtask02.R
import org.shirshov.testtask02.ui.activity.mainActivity
import org.shirshov.testtask02.ui.fragment.BaseFragment

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
