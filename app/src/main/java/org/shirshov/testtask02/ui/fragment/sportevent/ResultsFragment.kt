package org.shirshov.testtask02.ui.fragment.sportevent

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.shirshov.testtask02.R
import org.shirshov.testtask02.databinding.HeaderDateCellBinding
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
    private lateinit var adapter: CoreAdapter<ViewDataBinding, ResultItem>
    private lateinit var layoutManager: LinearLayoutManager
    private var skipUpdate = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        b = ResultsFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        adapter = object : CoreAdapter<ViewDataBinding, ResultItem>(viewModel.results.value!!) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CoreViewHolder(
                when (ResultItem.Type.values().getOrNull(viewType)) {
                    ResultItem.Type.HEADER_DATE -> HeaderDateCellBinding.inflate(inflater, parent, false)
                    ResultItem.Type.DATA -> ResultCellBinding.inflate(inflater, parent, false)
                    null -> throw IllegalStateException()
                }, viewLifecycleOwner
            )

            override fun bindViewHolder(holder: CoreViewHolder<ViewDataBinding>, item: ResultItem) {
                when (item.type) {
                    ResultItem.Type.HEADER_DATE -> (holder.b as HeaderDateCellBinding).item = item.headerDate
                    ResultItem.Type.DATA -> (holder.b as ResultCellBinding).item = item
                }
            }


            override fun getItemViewType(position: Int): Int {
                return adapter.items[position].type.ordinal
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
        var firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        if (adapter.items.getOrNull(firstVisibleItemPosition)?.type == ResultItem.Type.HEADER_DATE) {
            firstVisibleItemPosition++
        }
        if (firstVisibleItemPosition >= 0 && firstVisibleItemPosition < adapter.items.size) {
            setTitle(Format.dateAsMonth(adapter.items[firstVisibleItemPosition].data?.date))
        } else {
            setTitle(R.string.app_name)
        }
    }

}
