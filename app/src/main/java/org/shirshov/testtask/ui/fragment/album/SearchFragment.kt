package org.shirshov.testtask.ui.fragment.album

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.shirshov.testtask.R
import org.shirshov.testtask.databinding.SearchFragmentBinding
import org.shirshov.testtask.ui.fragment.BaseFragment

class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var b: SearchFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        b = SearchFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
//        b.viewModel = viewModel
        return b.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = "Search albums here..."
    }
}
