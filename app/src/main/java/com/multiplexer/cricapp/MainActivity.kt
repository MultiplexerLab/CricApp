package com.multiplexer.cricapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.irozon.sneaker.Sneaker
import com.irozon.sneaker.interfaces.OnSneakerClickListener
import com.multiplexer.cricapp.utlis.CheckNetworkConnection
import com.multiplexer.cricapp.utlis.Constants
import com.multiplexer.cricjass.R
import com.multiplexer.cricjass.databinding.ActivityMainBinding

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences
    private val internetPermissionCode = 100
    private lateinit var networkReceiver: BroadcastReceiver

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(Constants.preferenceName, Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        if (isFirstRun) {
            checkNetworkConnection()
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        }

        networkReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                checkNetworkConnection()
            }
        }

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkReceiver, filter)

        checkPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
//        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val bottomNavView: BottomNavigationView = findViewById(R.id.bottom_nav_view)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.rankingsFragment
            )
        )

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkNetworkConnection() {
        CheckNetworkConnection(application).observe(this) { isConnected ->
            if (isConnected) {
                Sneaker.with(this) // Activity, Fragment or ViewGroup
                    .setTitle("Success!!").setMessage("Internet Connected!!!").sneakSuccess()
            } else {
                Sneaker.with(this) // Activity, Fragment or ViewGroup
                    .setTitle("Error!!").setMessage("Internet Disconnected!!")
                    .setOnSneakerClickListener(object : OnSneakerClickListener {
                        override fun onSneakerClick(view: View) {
                            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                        }
                    }).sneakError()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver)
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.INTERNET
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.INTERNET), internetPermissionCode
            )
        }
    }
}