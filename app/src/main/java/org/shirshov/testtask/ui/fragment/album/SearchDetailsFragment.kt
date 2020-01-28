package org.shirshov.testtask.ui.fragment.album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.shirshov.testtask.R
import org.shirshov.testtask.data.Album
import org.shirshov.testtask.databinding.SearchDetailsFragmentBinding
import org.shirshov.testtask.databinding.SongCellBinding
import org.shirshov.testtask.ui.component.recycler.CoreAdapter
import org.shirshov.testtask.ui.component.recycler.CoreViewHolder
import org.shirshov.testtask.ui.fragment.BaseFragment
import org.shirshov.testtask.ui.holder.SongItem

class SearchDetailsFragment : BaseFragment() {

    private val viewModel: SearchDetailsViewModel by viewModel()
    private lateinit var b: SearchDetailsFragmentBinding
    private lateinit var adapter: CoreAdapter<SongCellBinding, SongItem>

    fun withParams(album: Album): SearchDetailsFragment {
        onInitBlock = { viewModel.album = album }
        return this
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        showBack(true)
        setTitle(viewModel.album.albumName)
        b = SearchDetailsFragmentBinding.inflate(inflater)
        initLoader(viewModel.loading, b.root as FrameLayout)
        setupLifecycle(viewModel, b)
        b.viewModel = viewModel
        Picasso.get().load(viewModel.album.cover).placeholder(R.drawable.ic_album_placeholder).into(b.imgCover)
        adapter = object : CoreAdapter<SongCellBinding, SongItem>(viewModel.songs.value!!) {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreViewHolder<SongCellBinding> {
                return CoreViewHolder(SongCellBinding.inflate(inflater, parent, false), viewLifecycleOwner)
            }

            override fun bindViewHolder(holder: CoreViewHolder<SongCellBinding>, item: SongItem) {
                holder.b.item = item
            }

        }
        b.recycler.adapter = adapter
        b.recycler.layoutManager = LinearLayoutManager(context)
        viewModel.songs.observe(viewLifecycleOwner) { items -> adapter.updateItems(items) }
        return b.root
    }

}
