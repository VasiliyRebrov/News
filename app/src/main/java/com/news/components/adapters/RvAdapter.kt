package com.news.components.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.data.db.Item
import com.news.R
import com.news.components.setSafeOnClickListener
import kotlinx.android.synthetic.main.card_item.view.*

class RvAdapter : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    private var itemList: MutableList<Item> = arrayListOf()
    var listener: Listener? = null

    interface Listener {
        fun onItemClick(item: Item)
        fun onDialogClick(item: Item)
    }


    class ViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_item, parent, false) as CardView
        return ViewHolder(cardView)
    }

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardView = holder.cardView
        val item = itemList[position]
        cardView.text_card_pubdate.text = item.pubDate
        cardView.text_card_title.text = item.title
        listener?.let { listener ->
            cardView.setSafeOnClickListener { listener.onItemClick(item) }
            cardView.but_card_open_dialog.setSafeOnClickListener { listener.onDialogClick(item) }
        }
    }

    fun updateList(newList: List<Item>) {
        if (newList != itemList) {
            this.itemList.clear()
            this.itemList.addAll(newList)
            notifyDataSetChanged()
        }
    }
}
