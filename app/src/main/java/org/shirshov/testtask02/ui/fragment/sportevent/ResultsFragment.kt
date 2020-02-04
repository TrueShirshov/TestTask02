package org.shirshov.testtask02.ui.fragment.sportevent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.shirshov.testtask02.databinding.ResultCellBinding
import org.shirshov.testtask02.databinding.ResultsFragmentBinding
import org.shirshov.testtask02.ui.component.recycler.CoreAdapter
import org.shirshov.testtask02.ui.component.recycler.CoreViewHolder
import org.shirshov.testtask02.ui.fragment.BaseFragment
import org.shirshov.testtask02.ui.holder.ResultItem

class ResultsFragment : BaseFragment() {

    private val sharedModel: SportSharedViewModel by sharedViewModel()
    private val viewModel: ResultsViewModel by viewModel { parametersOf(sharedModel) }
    private lateinit var b: ResultsFragmentBinding
    private lateinit var adapter: CoreAdapter<ResultCellBinding, ResultItem>

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
        b.recycler.layoutManager = LinearLayoutManager(context)
        viewModel.results.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        return b.root
    }

}
