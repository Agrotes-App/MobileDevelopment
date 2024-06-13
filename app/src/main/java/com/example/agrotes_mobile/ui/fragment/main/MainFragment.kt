package com.example.agrotes_mobile.ui.fragment.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.databinding.FragmentMainBinding
import com.example.agrotes_mobile.dummy.Model
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity
import com.example.agrotes_mobile.ui.fragment.history.HistoryAdapter

class MainFragment : Fragment() {
    private lateinit var binding : FragmentMainBinding
    private lateinit var recyclerView: RecyclerView
    private val data = ArrayList<Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView = binding.rvCommonProblems
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = MainAdapter(data)
        recyclerView.setHasFixedSize(true)

        data.addAll(getData())

        binding.fabScan.setOnClickListener{
            val intent = Intent(requireContext(), CameraActivity::class.java)
            startActivity(intent)
        }
    }

    // temporary function
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