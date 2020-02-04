package org.shirshov.testtask02.ui.component

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import org.shirshov.testtask02.databinding.TabbedViewPagerBinding
import org.shirshov.testtask02.ui.fragment.BaseFragment

class TabbedViewPager : FrameLayout {

    private lateinit var b: TabbedViewPagerBinding

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView(context)
    }

    private fun initView(context: Context) {
        b = TabbedViewPagerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun init(parentFragment: BaseFragment, fragments: List<Pair<String, BaseFragment>>) {
        b.viewPager.adapter = object : FragmentStateAdapter(parentFragment) {

            override fun getItemCount(): Int {
                return fragments.size
            }

            override fun createFragment(position: Int): Fragment {
                return fragments[position].second
            }
        }
        TabLayoutMediator(b.tabLayout, b.viewPager) { tab, position -> tab.text = fragments[position].first }.attach()
    }

}