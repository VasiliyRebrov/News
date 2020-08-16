package com.data.db

import androidx.room.Entity
import com.data.SEPARATOR

@Entity(tableName = "items", primaryKeys = ["category", "title"])
data class Item(
    val category: String,
    val title: String,
    val fullText: String,
    val enclosureUrl: String,
    val amplink: String,
    val description: String,
    val pubDate: String,
    val pubDateUnix: Long
) {
    companion object {
        fun createItemFromDataTransferList(DataTransferList: List<String>) = Item(
            DataTransferList[0],
            DataTransferList[1],
            DataTransferList[2],
            DataTransferList[3],
            DataTransferList[4],
            DataTransferList[5],
            DataTransferList[6],
            DataTransferList[7].toLong()
        )
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Item) return false
        return this.title == other.title
    }

    override fun toString() =
        category + SEPARATOR + title + SEPARATOR + fullText + SEPARATOR + enclosureUrl + SEPARATOR +
                amplink + SEPARATOR + description + SEPARATOR + pubDate + SEPARATOR + pubDateUnix

}