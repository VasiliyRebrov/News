package com.news.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.SEPARATOR
import com.data.db.Item
import com.news.viewmodel.FeedViewModel
import com.news.viewmodel.FeedViewModelFactory
import com.news.R
import com.news.components.adapters.RvAdapter
import com.news.components.snack
import com.news.databinding.FragmentFeedBinding
import kotlinx.android.synthetic.main.fragment_feed.*
import java.io.Serializable


class FeedFragment : Fragment() {
    private val viewModel: FeedViewModel by viewModels {
        FeedViewModelFactory(
            requireActivity().application,
            requireArguments().getString(STRING_DATA)!!
        )
    }
    private lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFeedBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_feed, container, false
            )
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        rootView = binding.root
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initObservers()
        initRecycler()
        initSwipeContainer()
    }

    private fun initSwipeContainer() {
        swipe_feed_root.setOnRefreshListener { viewModel.refreshData() }
        swipe_feed_root.setColorSchemeColors(
            ResourcesCompat.getColor(
                resources,
                R.color.colorAccent,
                requireContext().theme
            )
        )
    }

    private fun initObservers() {
        viewModel.getCorrespondenceLD().observe(viewLifecycleOwner, Observer {
            when {
                it.data != null -> rootView.snack(it.data)
                else -> rootView.snack(it.exception!!.message!!)
            }
        })
    }

    private fun initRecycler() {
        val adapter = RvAdapter()
        recycler_feed.adapter = adapter
        recycler_feed.layoutManager = LinearLayoutManager(requireContext())
        adapter.listener = object : RvAdapter.Listener {
            override fun onItemClick(item: Item) {
                val itemDto = item.toString().split(SEPARATOR)
                startActivity(Intent(activity, ShowItemActivity::class.java).apply {
                    putExtra(ShowItemActivity.EXTRA_DATA, itemDto as Serializable)
                })
            }

            override fun onDialogClick(item: Item) {
                ShowDescriptionDialog.newInstance(item.description)
                    .show(childFragmentManager, "dialog")
            }
        }
    }

    companion object {
        const val STRING_DATA = "data"

        @JvmStatic
        fun newInstance(value: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(STRING_DATA, value)
                }
            }
    }
}