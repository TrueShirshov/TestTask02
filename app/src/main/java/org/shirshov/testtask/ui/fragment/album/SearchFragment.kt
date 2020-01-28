package org.shirshov.testtask.ui.fragment.album

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.shirshov.testtask.R
import org.shirshov.testtask.databinding.AlbumCellBinding
import org.shirshov.testtask.databinding.SearchFragmentBinding
import org.shirshov.testtask.ui.component.recycler.CoreAdapter
import org.shirshov.testtask.ui.component.recycler.CoreViewHolder
import org.shirshov.testtask.ui.fragment.BaseFragment
import org.shirshov.testtask.ui.holder.AlbumItem
import org.shirshov.testtask.ui.navigation.Navigate
import org.shirshov.testtask.util.extension.hideKeyboard
import org.shirshov.testtask.util.extension.hideKeyboardOnScroll
import org.shirshov.testtask.util.extension.innerTextView
import org.shirshov.testtask.util.extension.setOnSearchAction
import java.util.concurrent.TimeUnit


class SearchFragment : BaseFragment() {

    private val viewModel: SearchViewModel by viewModel()
    private lateinit var b: SearchFragmentBinding
    private lateinit var adapter: CoreAdapter<AlbumCellBinding, AlbumItem>
    private lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        showBack(false)
        b = SearchFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        adapter = object : CoreAdapter<AlbumCellBinding, AlbumItem>(viewModel.albums.value!!) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreViewHolder<AlbumCellBinding> {
                val holder = CoreViewHolder(AlbumCellBinding.inflate(inflater, parent, false), viewLifecycleOwner)
                holder.onClick = { index -> Navigate.to(SearchDetailsFragment().withParams(adapter.items[index].data), this@SearchFragment) }
                return holder
            }

            override fun bindViewHolder(holder: CoreViewHolder<AlbumCellBinding>, item: AlbumItem) {
                holder.b.item = item
                Picasso.get().load(item.data.smallCover).placeholder(R.drawable.ic_album_placeholder).into(holder.b.imgCover)
            }

        }
        b.recycler.adapter = adapter
        b.recycler.layoutManager = LinearLayoutManager(context)
        viewModel.albums.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        return b.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.actionSearch).actionView as SearchView
        searchView.setIconifiedByDefault(false)
        searchView.maxWidth = Int.MAX_VALUE
        searchView.queryHint = getString(R.string.hint_query_albums)
        val subject = PublishSubject.create<String>()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                subject.onNext(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return onQueryTextSubmit(newText)
            }
        })
        disposable = subject.debounce(1, TimeUnit.SECONDS).subscribe { viewModel.search(it) }

        b.recycler.hideKeyboardOnScroll(activity, searchView)
        searchView.setOnSearchAction { activity?.hideKeyboard(searchView) }
        searchView.innerTextView.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                activity?.hideKeyboard(v)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
