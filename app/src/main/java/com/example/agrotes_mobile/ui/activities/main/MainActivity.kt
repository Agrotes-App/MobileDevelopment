package com.example.agrotes_mobile.ui.activities.main

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityMainBinding
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.ui.fragment.history.HistoryFragment
import com.example.agrotes_mobile.ui.fragment.home.HomeFragment
import com.example.agrotes_mobile.ui.fragment.profile.ProfileFragment
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getSession()
        initFragment()
        setupBottomBar()
        setupStatusBar()
    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            if (user.isLogin) {
                // nanti lakukan tutor !!!!!!!
            } else {
                intent = Intent(this@MainActivity, WelcomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            }
        }
    }

    private fun initFragment() {
        val homeFragment = HomeFragment()
        binding.bottomBar.setItemSelected(R.id.navigation_main)
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, homeFragment)
            .commit()
    }

    private fun setupBottomBar() {
        binding.bottomBar.setOnItemSelectedListener { menu ->
            when (menu) {
                R.id.navigation_main -> initFragment()
                R.id.navigation_history -> {
                    val historyFragment = HistoryFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, historyFragment)
                        .commit()
                }

                R.id.navigation_profile -> {
                    val profileFragment = ProfileFragment()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment, profileFragment)
                        .commit()
                }
            }
        }
    }
    @Suppress("DEPRECATION")
    private fun setupStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.background_primary)
    }
}