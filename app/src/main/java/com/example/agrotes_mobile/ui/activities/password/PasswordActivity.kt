package com.example.agrotes_mobile.ui.activities.password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.ActivityPasswordBinding
import com.example.agrotes_mobile.ui.activities.editProfile.EditProfileViewModel
import com.example.agrotes_mobile.ui.fragment.profile.ProfileViewModel
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory

class PasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPasswordBinding
    private val viewModel: PasswordViewModel by viewModels() {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPasswordBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupAction()
    }

    private fun setupAction() {
        binding.btnUpdate.setOnClickListener { updatePassword() }
    }

    private fun updatePassword() {
        val newPassword = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

        when{
            newPassword.isEmpty() -> {
                binding.edtPassword.setError("Password must not be empty", null)
                binding.edtConfirmPassword.requestFocus()
                return
            }
            confirmPassword.isEmpty() -> {
                binding.edtConfirmPassword.setError("Password must not be empty", null)
                binding.edtConfirmPassword.requestFocus()
                return
            }
            newPassword != confirmPassword -> {
                binding.edtConfirmPassword.setError("Password does not match", null)
                binding.edtConfirmPassword.requestFocus()
                return
            }
            else -> {
                viewModel.updateProfile(confirmPassword).observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            showLoading(true)
                        }

                        is Result.Success -> {
                            showLoading(false)
                            showToast(result.data.message)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}