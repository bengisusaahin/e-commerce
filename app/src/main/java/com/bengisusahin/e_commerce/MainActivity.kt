package com.bengisusahin.e_commerce

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.bengisusahin.e_commerce.databinding.ActivityMainBinding
import com.bengisusahin.e_commerce.databinding.DrawerHeaderBinding
import com.bengisusahin.e_commerce.util.RemoteConfigUtil
import com.bengisusahin.e_commerce.util.SharedPrefManager
import com.bengisusahin.e_commerce.viewmodel.AuthViewModel
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

// MainActivity class is annotated with @AndroidEntryPoint to generate the Hilt components
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var remoteConfigUtil: RemoteConfigUtil
    private val authViewModel : AuthViewModel by viewModels()
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerHeaderBinding: DrawerHeaderBinding
    @Inject lateinit var sharedPrefManager: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        drawerHeaderBinding = DrawerHeaderBinding.bind(binding.navigationView.getHeaderView(0))
        setContentView(binding.root)

        // Update the background color of the drawer layout observed from remote config
        remoteConfigUtil.colorLiveData.observe(this) { color ->
            Log.d("MainActivity", "Fetch successful, color: $color")
            binding.navigationView.setBackgroundColor(Color.parseColor(color))
            Log.d("MainActivity", "Background color set to $color on view ${binding.drawerLayout} and ${binding.navigationView}")
        }

        // firebase messaging intent extras
        Log.d("MainActivity", "Intent extras: ${intent.extras}")
        if (intent.getBooleanExtra("navigateToHome", false)) {
            Log.d("MainActivity", "navigateToHome extra is true, navigating to homeFragment")
            Handler(Looper.getMainLooper()).post {
                findNavController(R.id.fragmentContainerView).navigate(R.id.homeFragment)
            }
        }

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val drawerLayout = binding.drawerLayout
        val navView = binding.navigationView

        // Set up the drawer layout listener to update the header when the drawer is opened
        drawerLayout.addDrawerListener(object: DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
               // Log.d("MainActivity", "onDrawerSlide: $slideOffset")
            }
            override fun onDrawerOpened(drawerView: View) {
                sharedPrefManager.fetchFirstName()?.let {
                    drawerHeaderBinding.textView.text = "Welcome, $it!"
                }
                sharedPrefManager.fetchUserImage()?.let {
                    Glide.with(this@MainActivity)
                        .load(it)
                        .into(drawerHeaderBinding.imageView)
                }
            }

            override fun onDrawerClosed(drawerView: View) {
                Log.d("MainActivity", "onDrawerClosed")
            }

            override fun onDrawerStateChanged(newState: Int) {
                Log.d("MainActivity", "onDrawerStateChanged: $newState")
            }
        })
        // Set up the ActionBarDrawerToggle
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        toggle.drawerArrowDrawable.color= ContextCompat.getColor(this,R.color.white)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        // Set up the action bar for use with the NavController
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.toolbarTitle.text = destination.label
        }

        // Set up the navigation menu listener to navigate to the selected fragment
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
                            // Before navigating to loginFragment, pop the back stack
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