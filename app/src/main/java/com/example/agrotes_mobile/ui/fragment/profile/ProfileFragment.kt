package com.example.agrotes_mobile.ui.fragment.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.agrotes_mobile.databinding.FragmentProfileBinding
import com.example.agrotes_mobile.ui.activities.editProfile.EditProfileActivity
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
    }

    private fun setupAction() {
        with(binding) {
            btnEditProfile.setOnClickListener { toEditProfileActivity() }
            btnLogout.setOnClickListener { logout() }
        }
    }

    private fun logout() {
        val context = context
        val permissionsToRevoke: List<String> = mutableListOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context?.revokeSelfPermissionsOnKill(permissionsToRevoke)
        }

        viewModel.logOut()
        requireActivity()
            .finishAffinity()
    }

    private fun toEditProfileActivity() {
        val intent = Intent(requireContext(), EditProfileActivity::class.java)
        startActivity(intent)
    }

}