package com.news.components.adapters

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager.widget.ViewPager
import com.data.db.Item
import com.news.components.Status


@BindingAdapter("app:hiding")
fun hiding(view: View, activate: Status) {
    val isLoading = activate == Status.LOADING
    when (view) {
        is ProgressBar -> view.visibility = if (isLoading) View.VISIBLE else View.GONE
        is SwipeRefreshLayout -> view.isRefreshing = isLoading
    }
}

@BindingAdapter("app:hiding")
fun hiding(view: ViewPager, categoryList: List<String>?) {
    categoryList?.let {
        if (categoryList.isNotEmpty())
            (view.adapter as ViewPagerAdapter).updateList(categoryList)
    }
}

@BindingAdapter("app:hiding")
fun hiding(view: RecyclerView, categoryItemList: List<Item>?) {
    categoryItemList?.let {
        if (categoryItemList.isNotEmpty())
            (view.adapter as RvAdapter).updateList(categoryItemList)
    }
}

