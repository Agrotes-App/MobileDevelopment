package com.example.agrotes_mobile.ui.activities.welcome

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityWelcomeBinding
import com.example.agrotes_mobile.ui.activities.login.LoginActivity
import com.example.agrotes_mobile.ui.activities.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupView()
        setupAction()
    }

    // fullscreen
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    private fun setupAction() {
        with(binding) {
            onBackPressedDispatcher.addCallback { finishAffinity() }
            btnLogin.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@WelcomeActivity,
                        Pair(customBackgroundWelcome, "custom_background"),
                        Pair(ivAppIcon, "app_icon"),
                        Pair(btnLogin, "btn_login"),
                    )
                val intent = Intent(this@WelcomeActivity, LoginActivity::class.java)
                startActivity(intent, optionsCompat.toBundle())
            }

            btnSignup.setOnClickListener {
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        this@WelcomeActivity,
                        Pair(customBackgroundWelcome, "custom_background"),
                        Pair(ivAppIcon, "app_icon"),
                    )
                val intent = Intent(this@WelcomeActivity, SignupActivity::class.java)
                startActivity(intent, optionsCompat.toBundle())
            }
        }
    }
}