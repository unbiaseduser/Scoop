package taco.scoop.ui.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import rikka.lifecycle.sharedViewModels
import taco.scoop.R
import taco.scoop.core.MainViewModel
import taco.scoop.core.data.app.App
import taco.scoop.core.data.app.AppLoader
import taco.scoop.databinding.ActivityBlacklistAppsBinding
import taco.scoop.ui.adapter.AppAdapter
import taco.scoop.ui.helper.ToolbarElevationHelper
import taco.scoop.util.PreferenceHelper

class BlacklistAppsActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var mAdapter: AppAdapter
    private lateinit var binding: ActivityBlacklistAppsBinding
    private val mainViewModel: MainViewModel by sharedViewModels {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBlacklistAppsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.blacklistToolbar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.blacklistView.layoutManager = LinearLayoutManager(this)

        mAdapter = AppAdapter()
        binding.blacklistView.adapter = mAdapter

        mainViewModel.searchTerm.observe(this) {
            mAdapter.search(it)
        }

        mainViewModel.apps.observe(this) {
            if (it.isEmpty()) {
                AppLoader().loadData(this)
                updateViewStates(true)
            } else {
                mAdapter.setApps(it, PreferenceHelper.blacklistList)
                updateViewStates(false)
            }
        }

        ToolbarElevationHelper(binding.blacklistView, binding.blacklistToolbar.toolbar)
    }

    override fun onPause() {
        PreferenceHelper.editBlacklistPackages(mAdapter.selectedPackages)
        super.onPause()
    }

    private fun updateViewStates(loading: Boolean) {
        val empty = mAdapter.isEmpty
        // When one is visible, the other isn't (and vice versa)
        binding.blacklistProgressbar.isVisible = loading
        binding.blacklistView.isGone = loading || empty
    }

    fun onDataLoaded(apps: ArrayList<App?>?) {
        mainViewModel.setApps(apps)
        updateViewStates(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_blacklist, menu)
        val searchItem = menu.findItem(R.id.menu_blacklist_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(this)
        if ((mainViewModel.searchTerm.value as String).isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(mainViewModel.searchTerm.value, false)
        }
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        mainViewModel.setSearchTerm(newText)
        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
