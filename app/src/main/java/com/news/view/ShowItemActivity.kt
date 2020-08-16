package com.news.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.data.db.Item
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.news.R
import com.news.components.NO_NETWORK_MSG
import com.news.components.setSafeOnClickListener
import com.news.components.snack
import com.news.viewmodel.ShowItemViewModel
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_show_item.*
import kotlinx.android.synthetic.main.activity_show_item.appbar_show_item
import kotlin.math.abs


class ShowItemActivity : AppCompatActivity() {
    private val viewModel: ShowItemViewModel by viewModels()
    private val item by lazy {
        Item.createItemFromDataTransferList(
            intent.getStringArrayListExtra(
                EXTRA_DATA
            )!!
        )
    }
    private var appBarExpanded = true
    private var collapsedMenu: Menu? = null

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (collapsedMenu != null && (!appBarExpanded || collapsedMenu!!.size() != 1)) {
            collapsedMenu!!.add(R.string.external_link_action)
                .setIcon(R.drawable.baseline_open_in_new_white_24dp)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        collapsedMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when {
            item.itemId == android.R.id.home -> {
                finish()
                true
            }
            item.title == resources.getString(R.string.external_link_action) -> {
                viewModel.externalLinkWasClicked()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_item)
        initComponents()
    }

    private fun initComponents() {
        initObservation()
        initHeaderImage()
        initToolbar()
        initAppBar()
        fab_show_item_open_in_new.setSafeOnClickListener { viewModel.externalLinkWasClicked() }
        but_show_item_category.setSafeOnClickListener { finish() }
        initContent()
    }

    private fun initObservation() {
        viewModel.getExternalLinkEvent().observe(this, Observer {
            if (it) startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.amplink)))
            else parent_item.snack(NO_NETWORK_MSG, Snackbar.LENGTH_SHORT)
        })
    }

    private fun initHeaderImage() {
        if (item.enclosureUrl.isNotEmpty()) {
            Picasso.get().load(item.enclosureUrl)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .into(image_show_item_header)
        }
    }

    private fun initToolbar() {
        setSupportActionBar(tb_show_item)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.title = item.title
        }
    }

    private fun initAppBar() {
        appbar_show_item.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (abs(verticalOffset) > 200) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })
    }

    private fun initContent() {
        but_show_item_category.text = item.category
        text_show_item_pub_date.text = item.pubDate
        text_show_item_title.text = item.title
        text_show_item_full.text = item.fullText
    }

    companion object {
        const val EXTRA_DATA = "data"
    }
}
