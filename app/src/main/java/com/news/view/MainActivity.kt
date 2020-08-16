package com.news.view

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.news.R
import com.news.components.action
import com.news.components.adapters.ViewPagerAdapter
import com.news.components.snack
import com.news.databinding.ActivityMainBinding
import com.news.viewmodel.MainViewModel
import com.news.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels { MainViewModelFactory(application) }
    private lateinit var rootView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initComponents()
    }

    private fun initComponents() {
        initBinding()
        initObservers()
        initBar()
        initPager()

    }

    private fun initBinding() {
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
        rootView = binding.root
    }

    private fun initObservers() {
        viewModel.getCorrespondenceLD().observe(this, Observer {
            if (it.data != null)
                rootView.snack(it.data)
            else {
                val errorMsg = it.exception!!.message!!
                if (viewModel.isExistData())
                    rootView.snack(errorMsg)
                else
                    rootView.snack(errorMsg, Snackbar.LENGTH_INDEFINITE) {
                        this.action("обновить", null) { viewModel.refreshData() }
                    }
            }
        })
    }

    private fun initBar() {
        setSupportActionBar(tb_main)
    }

    private fun initPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        pager_main.adapter = adapter
        tab_main.setupWithViewPager(pager_main)
    }
}