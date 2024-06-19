package com.example.agrotes_mobile.ui.activities.editProfile

import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
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
        getSession()
    }

    private fun setupAction() {
        binding.btnUpdate.setOnClickListener { updateUserProfile() }
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
                    binding.edtUsername.setText(result.data.username)
                    binding.edtEmail.setText(result.data.email)
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
                else -> viewModel.updateProfile(username, email).observe(this@EditProfileActivity) { result ->
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

    private fun isValid(email: String): Boolean = !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun showToast(message: String?) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}