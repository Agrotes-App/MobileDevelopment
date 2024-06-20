package com.example.agrotes_mobile.ui.activities.editProfile

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityEditProfileBinding
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.utils.helper.errorIcon
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
        setupStatusBar()
        getSession()
    }

    private fun setupAction() {
        with(binding) {
            btnUpdate.setOnClickListener { updateUserProfile() }
            btnCancel.setOnClickListener { finish() }
        }
    }

    private fun getSession() {
        viewModel.getSession().observe(this) { user ->
            val id = user.userId
            getUserProfile(id)
        }
    }

    private fun getUserProfile(id: String?) {
        viewModel.getUserById(id).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    with(binding) {
                        edtUsername.setText(result.data.username)
                        edtEmail.setText(result.data.email)
                        Glide.with(this@EditProfileActivity)
                            .load(result.data.profilePhoto)
                            .into(ivProfilePhoto)
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun updateUserProfile() {
        with(binding) {
            val username = edtUsername.text.toString().trim()
            val email = edtEmail.text.toString().trim()

            when {
                username.isEmpty() -> errorIcon(edtUsername, getString(R.string.warning_username))
                email.isEmpty() -> errorIcon(edtEmail, getString(R.string.warning_email))
                !isValid(email) -> errorIcon(edtEmail, getString(R.string.error_invalid_email))
                else -> viewModel.updateProfile(username, email)
                    .observe(this@EditProfileActivity) { result ->
                        when (result) {
                            is Result.Loading -> {
                                showLoading(true)
                            }

                            is Result.Success -> {
                                showToast(result.data.message)
                                finish()
                                showLoading(false)
                            }

                            is Result.Error -> {
                                showLoading(false)
                                showToast(result.error)
                            }
                        }
                    }
            }
        }
    }

    private fun isValid(email: String): Boolean =
        !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun showToast(message: String?) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    @Suppress("DEPRECATION")
    private fun setupStatusBar() {
        val window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = this.resources.getColor(R.color.background_primary)
    }
}