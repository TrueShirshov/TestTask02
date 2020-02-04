package org.shirshov.testtask02.ui.fragment.sportevent

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.shirshov.testtask02.R
import org.shirshov.testtask02.databinding.SportEventsFragmentBinding
import org.shirshov.testtask02.ui.fragment.BaseFragment
import org.shirshov.testtask02.util.extension.toStringRes

class SportEventsFragment : BaseFragment() {

    private val sharedModel: SportSharedViewModel by sharedViewModel()
    private val viewModel: SportEventsViewModel by viewModel { parametersOf(sharedModel) }
    private lateinit var b: SportEventsFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setTitle(R.string.app_name)
        setHasOptionsMenu(true)
        b = SportEventsFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        b.tabbedViewPager.init(this, listOf(R.string.view_pager_fixtures.toStringRes() to FixturesFragment(), R.string.view_pager_results.toStringRes() to ResultsFragment()))
        return b.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.actionFilter -> {
                showBottomDialog(FilterFragment()) { viewModel.onFilterEditDone() }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
