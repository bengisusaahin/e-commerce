package com.bengisusahin.e_commerce

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bengisusahin.e_commerce.databinding.ActivityMainBinding
import com.bengisusahin.e_commerce.util.RemoteConfigUtil
import com.bengisusahin.e_commerce.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.log

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var remoteConfigUtil: RemoteConfigUtil
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Renk bilgisi ile arka plan rengini güncelle
        remoteConfigUtil.colorLiveData.observe(this) { color ->
            // Arka plan rengini güncelle
            Log.d("MainActivity", "Fetch successful, color: $color") // Fetch başarılı ve renk değerini logla
            binding.navigationView.setBackgroundColor(Color.parseColor(color))
            Log.d("MainActivity", "Background color set to $color on view ${binding.drawerLayout} and ${binding.navigationView}") // Arka plan rengini ayarlama işlemi sonrası log
        }

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
                R.id.logout -> {
                    AlertDialog.Builder(this)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout?")
                        .setPositiveButton("Yes") { _, _ ->
                            authViewModel.logout()
                            Log.d("MainActivity", "Navigating to loginFragment")
                            navController.popBackStack()
                            navController.navigate(R.id.loginFragment)
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
            }
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }
}