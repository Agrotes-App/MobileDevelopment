package com.example.agrotes_mobile.ui.fragment.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.FragmentHistoryBinding
import com.example.agrotes_mobile.dummy.Model

class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var recyclerView: RecyclerView
    private val data = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        recyclerView = binding.rvHistory
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = HistoryAdapter(data)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        recyclerView.addItemDecoration(itemDecoration)
        recyclerView.setHasFixedSize(true)

        data.addAll(getData())

    }

    private fun getData(): ArrayList<Model> {
        val photo = resources.getStringArray(R.array.data_photo)
        val data = ArrayList<Model>()

        for (i in photo.indices) {
            val model = Model(photo[i])
            data.add(model)
        }
        return data
    }


}