package com.news.viewmodel

import android.app.Application
import com.data.Repository
import com.news.components.NoNetworkException
import com.news.components.checkInternetAccess

class FeedViewModel(
    application: Application, private val repository: Repository, categoryName: String
) : BaseViewModel<String>(application) {
    val itemList = repository.getItemList(categoryName)

    override fun refreshData() {
        runCoroutine {
            if (checkInternetAccess(getApplication())) return@runCoroutine repository.refreshData()
            throw NoNetworkException()
        }
    }
}