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
import org.shirshov.testtask02.databinding.FilterCellBinding
import org.shirshov.testtask02.databinding.FilterFragmentBinding
import org.shirshov.testtask02.ui.component.recycler.CoreAdapter
import org.shirshov.testtask02.ui.component.recycler.CoreViewHolder
import org.shirshov.testtask02.ui.fragment.BaseFragment
import org.shirshov.testtask02.ui.holder.FilterItem
import org.shirshov.testtask02.util.extension.inverse

class FilterFragment : BaseFragment() {

    private val sharedModel: SportSharedViewModel by sharedViewModel()
    private val viewModel: FilterViewModel by viewModel { parametersOf(sharedModel) }
    private lateinit var b: FilterFragmentBinding
    private lateinit var adapter: CoreAdapter<FilterCellBinding, FilterItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        b = FilterFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        adapter = object : CoreAdapter<FilterCellBinding, FilterItem>(viewModel.filter.value!!) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreViewHolder<FilterCellBinding> {
                val holder = CoreViewHolder(FilterCellBinding.inflate(inflater, parent, false), viewLifecycleOwner)
                holder.onClick = { index -> adapter.items[index].data.active = adapter.items[index].checked.inverse() }
                return holder
            }

            override fun bindViewHolder(holder: CoreViewHolder<FilterCellBinding>, item: FilterItem) {
                holder.b.item = item
            }

        }
        b.recycler.adapter = adapter
        b.recycler.layoutManager = LinearLayoutManager(context)
        viewModel.filter.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        return b.root
    }

}
