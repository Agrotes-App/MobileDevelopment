package com.example.agrotes_mobile.ui.activities.signup

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
import com.example.agrotes_mobile.databinding.ActivitySignupBinding
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.ui.activities.login.LoginActivity
import com.example.agrotes_mobile.utils.helper.error
import com.example.agrotes_mobile.utils.helper.errorIcon
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
            btnSignup.setOnClickListener { signup() }
            tvHaveAccount.setOnClickListener { toSignupActivity() }
            onBackPressedDispatcher.addCallback { goBack() }
        }
    }


    private fun signup() {
        with(binding) {
            val username = edtUsername.text.toString().trim()
            val email = edtEmailRegister.text.toString().trim()
            val password = edtPasswordRegister.text.toString().trim()

            when {
                username.isEmpty() -> errorIcon(edtUsername, getString(R.string.warning_username))
                email.isEmpty() -> errorIcon(edtEmailRegister, getString(R.string.warning_email))
                password.isEmpty() -> error(edtPasswordRegister, getString(R.string.warning_password))
                !isValid(email) -> errorIcon(edtEmailRegister, getString(R.string.error_invalid_email))
                password.length < 6 -> error(edtPasswordRegister, getString(R.string.warning_password_lenght))
                else -> {
                    lifecycleScope.launch {
                        viewModel.signup(username, email, password).observe(this@SignupActivity) { result ->
                            if (result != null) {
                                when (result) {
                                    is Result.Loading -> {
                                        showLoading(true)
                                    }

                                    is Result.Success -> {
                                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()

                                        showToast(getString(R.string.signup_success))
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
    }

    private fun toSignupActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun goBack() {
        val optionsCompat: ActivityOptionsCompat =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                this,
                Pair(binding.customBackgroundRegister, "custom_background"),
                Pair(binding.ivAppIcon, "app_icon")
            )
        val intent = Intent(this, WelcomeActivity::class.java)
        startActivity(intent, optionsCompat.toBundle())
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