package com.erick.frontendavanzado.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.get
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.erick.frontendavanzado.R
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {

    private lateinit var toolBar: MaterialToolbar
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.fragmentContainer_mainActivity
        ) as NavHostFragment

        navController = navHostFragment.navController

        val appbarConfig = AppBarConfiguration(setOf(R.id.loginFragment, R.id.charactersFragment))
        toolBar = findViewById(R.id.toolbar_mainActivity)
        toolBar.setupWithNavController(navController, appbarConfig)

        listenToNavGraphChanges()
    }

    private fun listenToNavGraphChanges() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    toolBar.visibility = View.GONE
                }
                R.id.charactersFragment -> {
                    toolBar.visibility = View.VISIBLE
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
                    supportActionBar?.setDisplayShowHomeEnabled(true)
                    //toolbar.navigationIcon = null
                    toolBar.title = "Characters"
                    toolBar.menu.findItem(R.id.menu_item_sortAZ).isVisible = true
                    toolBar.menu.findItem(R.id.menu_item_sortZA).isVisible = true
                    toolBar.menu.findItem(R.id.menu_item_logOut).isVisible = true
                    toolBar.menu.findItem(R.id.menu_item_update).isVisible = true
                    toolBar.menu.findItem(R.id.menu_item_delete).isVisible = false
                }

                R.id.characterDetailFragment -> {
                    toolBar.visibility = View.VISIBLE
                    toolBar.title = "Character Detail"
                    toolBar.menu.findItem(R.id.menu_item_sortAZ).isVisible = false
                    toolBar.menu.findItem(R.id.menu_item_sortZA).isVisible = false
                    toolBar.menu.findItem(R.id.menu_item_logOut).isVisible = false
                    toolBar.menu.findItem(R.id.menu_item_update).isVisible = true
                    toolBar.menu.findItem(R.id.menu_item_delete).isVisible = true
                }
            }
        }
    }

    public fun getToolBar(): MaterialToolbar{
        return toolBar
    }
}