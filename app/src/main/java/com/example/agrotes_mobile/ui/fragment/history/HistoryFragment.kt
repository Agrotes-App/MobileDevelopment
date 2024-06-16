package com.example.agrotes_mobile.ui.fragment.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.agrotes_mobile.data.local.entity.DiseaseEntity
import com.example.agrotes_mobile.databinding.FragmentHistoryBinding
import com.example.agrotes_mobile.ui.adapter.HistoryAdapter
import com.example.agrotes_mobile.utils.ViewModelFactory

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewmodel: HistoryViewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getAllHistory()
        setupView()
    }

    private fun getAllHistory() {
        viewmodel.getAllHistory().observe(viewLifecycleOwner) { result ->
            setupAdapter(result)
        }
    }

    private fun setupAdapter(result: List<DiseaseEntity>?){
        val adapter = HistoryAdapter()
        adapter.submitList(result)
        binding.rvHistory.adapter = adapter
    }

    private fun setupView() {
        val layoutManager = LinearLayoutManager(context)
        with(binding) {
            rvHistory.layoutManager = layoutManager
            val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
            rvHistory.addItemDecoration(itemDecoration)
            rvHistory.setHasFixedSize(true)
        }
    }
}
