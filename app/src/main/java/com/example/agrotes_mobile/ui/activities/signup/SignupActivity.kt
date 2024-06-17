package com.example.agrotes_mobile.ui.activities.signup

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
import com.example.agrotes_mobile.databinding.ActivitySignupBinding
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.ui.activities.login.LoginActivity
import com.example.agrotes_mobile.utils.ViewModelFactory
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel: SignupViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
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
            btnSignup.setOnClickListener { signup() }
            tvHaveAccount.setOnClickListener { toSignupActivity() }
            onBackPressedDispatcher.addCallback { goBack() }
        }
    }


    private fun signup() {
        val username = binding.edtUsername.text.toString().trim()
        val email = binding.edtEmailRegister.text.toString().trim()
        val password = binding.edtPasswordRegister.text.toString().trim()

        lifecycleScope.launch {
            viewModel.signup(username, email, password).observe(this@SignupActivity) { result ->

                Log.d("DEBUG", "Result: $username, $email, $password")

                if (result != null) {
                    when (result){
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            val message = result.data.createdUser?.id.toString()

                            Log.d("DEBUGG",message)

                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()

                            showToast(getString(R.string.signup_success))
                            Log.d(TAG, "Success: $message")
                            showLoading(false)
                        }

                        is Result.Error -> {
                            showToast(result.error)
                            Log.e(TAG, "Error: ${result.error}")
                            showLoading(false)
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

    // Handle back button
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        private const val TAG = "SignupActivity"
    }
}