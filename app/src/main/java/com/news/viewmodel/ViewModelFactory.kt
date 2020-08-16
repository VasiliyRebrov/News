package com.news.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.data.Repository

class FeedViewModelFactory(
    private val application: Application,
    private val categoryName: String
) : ViewModelProvider.NewInstanceFactory() {
    private val repository = Repository.getRepository(application)

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        FeedViewModel(application, repository, categoryName) as T
}

class MainViewModelFactory(private val application: Application) :
    ViewModelProvider.NewInstanceFactory() {
    private val repository = Repository.getRepository(application)

    override fun <T : ViewModel?> create(modelClass: Class<T>) =
        MainViewModel(application, repository) as T
}
