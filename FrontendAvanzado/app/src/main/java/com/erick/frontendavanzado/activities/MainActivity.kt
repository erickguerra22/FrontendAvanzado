package com.erick.frontendavanzado.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val appbarConfig = AppBarConfiguration(navController.graph)
        toolBar = findViewById(R.id.toolbar_mainActivity)
        toolBar.setupWithNavController(navController, appbarConfig)

        listenToNavGraphChanges()
    }

    private fun listenToNavGraphChanges() {
        navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener{controller, destination, arguments ->
            when (destination.id){
                R.id.characterDetailFragment -> {
                    toolBar.menu.clear()
                }
                else ->{
                    toolBar.menu.clear()
                    toolBar.inflateMenu(R.menu.tool_bar_menu)
                }
            }
        })
    }

    public fun getToolBar(): MaterialToolbar{
        return toolBar
    }
}