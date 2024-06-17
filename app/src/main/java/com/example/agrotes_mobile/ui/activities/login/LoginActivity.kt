package com.example.agrotes_mobile.ui.activities.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.data.pref.UserModel
import com.example.agrotes_mobile.databinding.ActivityLoginBinding
import com.example.agrotes_mobile.ui.activities.main.MainActivity
import com.example.agrotes_mobile.ui.activities.signup.SignupActivity
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.utils.ViewModelFactory
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        setupView()
    }


    private fun setupAction() {
        with(binding) {
            btnLogin.setOnClickListener { login() }
            tvDontHaveAccount.setOnClickListener { toSignup() }
            onBackPressedDispatcher.addCallback { goBack() }
        }
    }

    private fun login() {
        val email = binding.edtEmailLogin.text.toString().trim()
        val password = binding.edtPasswordLogin.text.toString().trim()

        lifecycleScope.launch {
            viewModel.login(email, password).observe(this@LoginActivity) { result ->
                when (result) {
                    is Result.Loading -> {
                        showLoading(true)
                    }

                    is Result.Success -> {
                        val name = result.data.user?.username.toString()
                        val userId = result.data.user?.id.toString()
                        val token = result.data.token.toString()
                        val message = result.data.message.toString()

                        Log.d("RESPONSE", token)

                        viewModel.saveSession(
                            UserModel(
                                name = name,
                                userId = userId,
                                token = token,
                                isLogin = true
                            )
                        )

                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finishAffinity()

                        Log.d(TAG, message)
                        showToast(getString(R.string.login_success))
                        showLoading(false)
                    }

                    is Result.Error -> {
                        showToast(result.error)
                        showLoading(false)
                    }
                }
            }
        }
    }

    private fun toSignup() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    // Handle back button
    private fun goBack() {
        with(binding) {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@LoginActivity,
                    Pair(customBackgroundLogin, "custom_background"),
                    Pair(ivAppIcon, "app_icon"),
                    Pair(btnLogin, "btn_login")
                )
            val intent = Intent(this@LoginActivity, WelcomeActivity::class.java)
            startActivity(intent, optionsCompat.toBundle())
        }
    }

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val TAG = "LoginActivity"
    }
}