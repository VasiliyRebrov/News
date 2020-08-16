package com.news.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.news.components.*

class ShowItemViewModel(application: Application) : AndroidViewModel(application) {
    private val externalLinkEvent = SingleLiveEvent<Boolean>()
    fun getExternalLinkEvent(): LiveData<Boolean> = externalLinkEvent

    fun externalLinkWasClicked() {
        externalLinkEvent.value = checkInternetAccess(getApplication())
    }
}