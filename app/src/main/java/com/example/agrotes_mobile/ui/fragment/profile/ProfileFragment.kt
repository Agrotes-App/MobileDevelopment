package com.example.agrotes_mobile.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.agrotes_mobile.databinding.FragmentProfileBinding
import com.example.agrotes_mobile.ui.activities.editProfile.EditProfileActivity
import com.example.agrotes_mobile.utils.ViewModelFactory

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by viewModels{
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction() {
        with(binding){
            btnLogout.setOnClickListener { logout() }
            btnEditProfile.setOnClickListener { toEditProfileActivity() }
        }
    }

    private fun logout() {
        viewModel.logOut()
        requireActivity().finishAffinity()
    }

    private fun toEditProfileActivity() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }

}