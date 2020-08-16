package com.news.components.adapters

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.news.view.FeedFragment

class ViewPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val categoryList: MutableList<String> = arrayListOf()
    override fun getItem(position: Int) = FeedFragment.newInstance(categoryList[position])

    override fun getCount() = categoryList.size

    override fun getPageTitle(position: Int) = categoryList[position]

    fun updateList(newList: List<String>) {
        if (newList != categoryList) {
            this.categoryList.clear()
            this.categoryList.addAll(newList)
            notifyDataSetChanged()
        }
    }
}