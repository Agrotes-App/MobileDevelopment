package com.example.agrotes_mobile.ui.activities.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
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
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.data.local.entity.UserEntity
import com.example.agrotes_mobile.databinding.ActivityLoginBinding
import com.example.agrotes_mobile.ui.activities.main.MainActivity
import com.example.agrotes_mobile.ui.activities.signup.SignupActivity
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory
import kotlinx.coroutines.launch
import com.example.agrotes_mobile.utils.helper.error
import com.example.agrotes_mobile.utils.helper.errorIcon


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()

        setContentView(binding.root)
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
        with(binding) {
            val email = edtEmailLogin.text.toString().trim()
            val password = edtPasswordLogin.text.toString().trim()

            when {
                email.isEmpty() -> errorIcon(edtEmailLogin, getString(R.string.warning_email))
                password.isEmpty() -> error(edtPasswordLogin, getString(R.string.warning_password))
                password.length < 6 -> error(edtPasswordLogin, getString(R.string.warning_password_lenght))
                !isValid(email) -> error(edtEmailLogin, getString(R.string.error_invalid_email))
                else -> {
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

                                    // Save session
                                    viewModel.saveSession(
                                        UserEntity(
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
            }
        }
    }

    private fun toSignup() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goBack() {
        with(binding) {
            val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
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

    private fun isValid(email: String): Boolean = !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}