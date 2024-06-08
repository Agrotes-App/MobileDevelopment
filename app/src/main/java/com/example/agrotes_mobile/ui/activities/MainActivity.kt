package com.example.agrotes_mobile.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityMainBinding
import com.example.agrotes_mobile.ui.fragment.HistoryFragment
import com.example.agrotes_mobile.ui.fragment.MainFragment
import com.example.agrotes_mobile.ui.fragment.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

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

        initFragment()
        setupBottomBar()
        setupButton()

    }

    private fun initFragment() {
        binding.bottomBar.setItemSelected(R.id.navigation_main)

        val mainFragment = MainFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, mainFragment)
            .commit()
    }

    private fun setupBottomBar() {
        binding.bottomBar.setOnItemSelectedListener {menu ->
            when(menu){
                R.id.navigation_main -> {
                   initFragment()
                }
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

    private fun setupButton() {
        binding.btn.setOnClickListener {
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
        }
    }
}