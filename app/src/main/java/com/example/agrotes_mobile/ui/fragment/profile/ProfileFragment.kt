package com.example.agrotes_mobile.ui.fragment.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.FragmentProfileBinding
import com.example.agrotes_mobile.ui.activities.editProfile.EditProfileActivity
import com.example.agrotes_mobile.ui.activities.password.PasswordActivity
import com.example.agrotes_mobile.ui.activities.welcome.WelcomeActivity
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSession()
        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            btnEditProfile.setOnClickListener { toEditProfileActivity() }
            btnChangePassword.setOnClickListener { toChangePasswordActivity() }
            btnLogout.setOnClickListener { logout() }
        }
    }

    private fun getSession() {
        viewModel.getSession().observe(requireActivity()) { user ->
            val id = user.userId
            getUserProfile(id)
        }
    }

    private fun getUserProfile(id: String?) {
        viewModel.getUserById(id).observe(requireActivity()) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    with(binding) {
                        tvUsername.text = result.data.username
                        tvEmail.text = result.data.email
                        Glide
                            .with(requireContext())
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

    private fun toEditProfileActivity() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }


    private fun toChangePasswordActivity() {
        val intent = Intent(requireContext(), PasswordActivity::class.java)
        startActivity(intent)
    }

    private fun logout() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.alert_title_logout))
            .setMessage(getString(R.string.alert_message_logout))
            .setPositiveButton(getString(R.string.alert_positive_button)) { _, _ ->
                viewModel.logOut()
                val intent = Intent(requireContext(), WelcomeActivity::class.java)
                startActivity(intent)
                finishAffinity(requireActivity())
            }
            .setNegativeButton(getString(R.string.alert_negative_button)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showToast(message: String?) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


}