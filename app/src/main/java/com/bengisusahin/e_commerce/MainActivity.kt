package com.bengisusahin.e_commerce

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bengisusahin.e_commerce.databinding.ActivityMainBinding
import com.bengisusahin.e_commerce.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("MainActivity", "Intent extras: ${intent.extras}")
        if (intent.getBooleanExtra("navigateToHome", false)) {
            Log.d("MainActivity", "navigateToHome extra is true, navigating to homeFragment")
            Handler(Looper.getMainLooper()).post {
                findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)
            }
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        //val navController = findNavController(R.id.fragmentContainerView)
        val drawerLayout = binding.drawerLayout
        val navView = binding.navigationView
        // Set up the ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // Set up the action bar for use with the NavController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                }
                R.id.categoriesFragment -> {
                    navController.navigate(R.id.categoriesFragment)
                }
                R.id.searchFragment -> {
                    navController.navigate(R.id.searchFragment)
                }
                R.id.favoritesFragment -> {
                    navController.navigate(R.id.favoritesFragment)
                }
                R.id.ordersFragment -> {
                    navController.navigate(R.id.ordersFragment)
                }
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                }
                R.id.loginFragment -> {
                    AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes") { _, _ ->
                            authViewModel.logout()
                            Log.d("MainActivity", "Navigating to loginFragment")
                            navController.navigate(R.id.loginFragment)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
            true
        }
        // TODO
        // Observe the isLoggedIn LiveData
        authViewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn) {
                Log.d("MainActivity", "onCreate: $isLoggedIn")
                // User logged in, show the home screen
                Log.d("MainActivity", "User logged in, navigating to homeFragment")
                navController.navigate(R.id.homeFragment)
                // Remove loginFragment from the back stack
                navController.popBackStack(R.id.loginFragment, true)
            } else {
                Log.d("MainActivitylogout", "onCreate: $isLoggedIn")
                // User logged out, show the login screen
                Log.d("MainActivity", "User logged out, popping back stack")
                navController.popBackStack(R.id.loginFragment, true)
                Log.d("MainActivity", "Navigating to loginFragment")
                navController.navigate(R.id.loginFragment)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}