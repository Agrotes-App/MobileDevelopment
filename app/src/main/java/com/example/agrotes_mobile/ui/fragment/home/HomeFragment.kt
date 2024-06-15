package com.example.agrotes_mobile.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.data.remote.responses.ListStoryItem
import com.example.agrotes_mobile.databinding.FragmentHomeBinding
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity
import com.example.agrotes_mobile.utils.ViewModelFactory

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupCommonProblems()
        setupView()
    }

    private fun setupAction() {
        with(binding) {
            fabScan.setOnClickListener { toCameraActivity() }
            btnScan.setOnClickListener { toCameraActivity() }
        }
    }

    private fun setupCommonProblems() {
        viewModel.getAllDisease().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    setupAdapter(result.data.listStory)
                    Log.d(TAG, result.data.listStory.toString())
                    showLoading(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    showToast(result.error)
                }
            }

        }
    }

    private fun toCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun setupAdapter(listStoryItem: List<ListStoryItem?>?) {
        val adapter = HomeAdapter()
        adapter.submitList(listStoryItem)
        binding.rvCommonProblems.adapter = adapter
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        with(binding) {
            rvCommonProblems.layoutManager = layoutManager
            rvCommonProblems.setHasFixedSize(true)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object{
        const val TAG = "HomeFragment"
    }
}