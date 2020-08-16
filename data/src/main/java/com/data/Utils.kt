package com.data


import com.data.db.Item
import com.data.remote.FeedService
import com.data.remote.entities.RssItem
import com.data.remote.entities.RssRoot
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Throws(IOException::class)
fun parseNewItems(): MutableList<RssItem> {
    var body: RssRoot? = null
    val retrofit = Retrofit.Builder().baseUrl("https://www.vesti.ru/")
        .addConverterFactory(SimpleXmlConverterFactory.create()).build()
    val inter = retrofit.create(FeedService::class.java)
    val call = inter.loadRSSFeed()
    val response = call.execute()
    if (response.isSuccessful) body = response.body()
    if (!response.isSuccessful || body == null) throw IOException()
    return body.rssItemList
}

fun List<RssItem>.convertToEntity() =
    List(this.size) {
        fun convertToUnix(pubDate: String): Long {
            val sourceFormat = SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
            return try {
                sourceFormat.parse(pubDate)!!.time
            } catch (e: ParseException) {
                1
            }
        }

        fun convertToLocalDate(pubDateUnix: Long): String {
            val requireFormat = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())
            val date = Date(pubDateUnix)
            return requireFormat.format(date)
        }

        val rssItem = this[it]
        val pubDateUnix = convertToUnix(rssItem.pubDate)
        val pubDate = convertToLocalDate(pubDateUnix)

        return@List Item(
            rssItem.category ?: "без категории",
            rssItem.title,
            rssItem.fullText?: "",
            rssItem.enclosureUrl?: "",
            rssItem.amplink?: "",
            rssItem.description?: "",
            pubDate,
            pubDateUnix
        )
    }