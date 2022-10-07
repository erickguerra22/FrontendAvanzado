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
        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener{controller, destination, arguments ->
            when (destination.id){
                R.id.loginFragment -> {
                    toolBar.visibility = View.GONE
                }
                R.id.characterDetailFragment -> {
                    toolBar.menu.clear()
                    toolBar.inflateMenu(R.menu.character_detail_tool_bar_menu)
                    toolBar.visibility = View.VISIBLE
                }
                else ->{
                    toolBar.menu.clear()
                    toolBar.inflateMenu(R.menu.characters_tool_bar_menu)
                    toolBar.visibility = View.VISIBLE
                }
            }
        })
    }

    public fun getToolBar(): MaterialToolbar{
        return toolBar
    }
}