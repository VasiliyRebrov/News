package com.data.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface ItemDao {

    @Query("SELECT category FROM items ")
    fun getCategoryList(): LiveData<List<String>>

    @Query("SELECT category FROM items ")
    fun getActualCategoryList(): List<String>

    @Query("SELECT * FROM items where category=:categoryName ORDER BY pubDateUnix DESC ")
    fun getItemListByCategory(categoryName: String): LiveData<List<Item>>

    @Query("SELECT * FROM items")
    fun getItemList(): List<Item>

    @Transaction
    fun refreshData(oldItems: List<Item>, newItems: List<Item>): Pair<Int, Int> {
        val deletedCount = delete(oldItems)
        val addedCount = insert(newItems)
        return Pair(deletedCount, addedCount.size)
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(itemList: List<Item>): List<Long>

    @Delete
    fun delete(itemList: List<Item>): Int
}