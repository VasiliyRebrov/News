package com.news.viewmodel

import android.app.Application
import com.data.Repository
import com.news.components.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(application: Application, private val repository: Repository) :
    BaseViewModel<String>(application) {
    val categoryList = repository.getCategoryList()

    init {
        refreshData()
    }

    override fun refreshData() {
        runCoroutine {
            if (checkInternetAccess(getApplication())) return@runCoroutine repository.refreshData()
            throw NoNetworkException()
        }
    }

    fun isExistData(): Boolean {
        var isExist = false
        runBlocking {
            isExist = withContext(Dispatchers.IO) {
                repository.getActualData().isNotEmpty()
            }
        }
        return isExist
    }
}
