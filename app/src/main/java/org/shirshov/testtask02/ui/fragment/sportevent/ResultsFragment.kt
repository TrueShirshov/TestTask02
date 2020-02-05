package org.shirshov.testtask02.ui.fragment.sportevent

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.shirshov.testtask02.R
import org.shirshov.testtask02.databinding.ResultCellBinding
import org.shirshov.testtask02.databinding.ResultsFragmentBinding
import org.shirshov.testtask02.ui.component.recycler.CoreAdapter
import org.shirshov.testtask02.ui.component.recycler.CoreViewHolder
import org.shirshov.testtask02.ui.fragment.BaseFragment
import org.shirshov.testtask02.ui.holder.ResultItem
import org.shirshov.testtask02.ui.util.Format

class ResultsFragment : BaseFragment() {

    private val sharedModel: SportSharedViewModel by sharedViewModel()
    private val viewModel: ResultsViewModel by viewModel { parametersOf(sharedModel) }
    private lateinit var b: ResultsFragmentBinding
    private lateinit var adapter: CoreAdapter<ResultCellBinding, ResultItem>
    private lateinit var layoutManager: LinearLayoutManager
    private var skipUpdate = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        b = ResultsFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        adapter = object : CoreAdapter<ResultCellBinding, ResultItem>(viewModel.results.value!!) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreViewHolder<ResultCellBinding> {
                return CoreViewHolder(ResultCellBinding.inflate(inflater, parent, false), viewLifecycleOwner)
            }

            override fun bindViewHolder(holder: CoreViewHolder<ResultCellBinding>, item: ResultItem) {
                holder.b.item = item
            }

        }
        b.recycler.adapter = adapter
        layoutManager = LinearLayoutManager(context)
        b.recycler.layoutManager = layoutManager
        b.recycler.onScroll { updateTitle() }
        skipUpdate = true
        viewModel.results.observe(viewLifecycleOwner) { items ->
            adapter.updateItems(items)
            Handler().post { updateTitle() } // waiting notifyDataSetChanged completion
        }
        return b.root
    }

    private fun updateTitle() {
        if (skipUpdate) {
            skipUpdate = false
            return
        }
        if (!isVisible) return
        val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < adapter.items.size) {
            setTitle(Format.dateAsMonth(adapter.items[firstVisibleItemPosition].data.date))
        } else {
            setTitle(R.string.app_name)
        }
    }

}
