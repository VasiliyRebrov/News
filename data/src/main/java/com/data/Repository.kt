package com.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.data.db.AppDB
import com.data.db.Item
import com.data.db.ItemDao


class Repository private constructor(private val dao: ItemDao) {
    fun getCategoryList(): LiveData<List<String>> {
        return Transformations.map(dao.getCategoryList()) { sourceList ->
            val mediatorMap = mutableMapOf<String, Int>()
            sourceList.forEach { element ->
                mediatorMap[element] = mediatorMap[element]?.let { it + 1 } ?: 1
            }
            return@map mediatorMap.toList().sortedByDescending { it.second }.map { it.first }
        }
    }

    fun getItemList(categoryName: String) = dao.getItemListByCategory(categoryName)

    fun getActualData() = dao.getActualCategoryList()

    fun refreshData(): String {
        val currentItems = dao.getItemList()
        val newItems = parseNewItems().convertToEntity()
        return if (currentItems.sortedBy { it.pubDateUnix } != newItems.sortedBy { it.pubDateUnix })
            refreshDb(currentItems, newItems)
        else
            return "данные и так актуальны"
    }

    private fun refreshDb(currentItems: List<Item>, newItems: List<Item>): String {
        val toRemoveItems = with(currentItems.toMutableSet()) {
            removeAll(newItems)
            toList()
        }
        val toInsertItems = with(newItems.toMutableSet()) {
            removeAll(currentItems)
            toList()
        }
        val (deletedItems, addedItems) = dao.refreshData(toRemoveItems, toInsertItems)
        return "добавлено $addedItems элементов\nудалено $deletedItems элементов"
    }

    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getRepository(context: Context): Repository {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val dao = AppDB.getDatabase(context).itemDao()
                val instance = Repository(dao)
                INSTANCE = instance
                return instance
            }
        }
    }
}
