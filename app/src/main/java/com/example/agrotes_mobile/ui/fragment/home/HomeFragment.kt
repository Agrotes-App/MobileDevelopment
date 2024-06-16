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
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.data.remote.test.ListStoryItem
import com.example.agrotes_mobile.databinding.FragmentHomeBinding
import com.example.agrotes_mobile.ui.adapter.DiseaseAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupCommonProblems()
        setupAdapterView()

        getWeather()
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
                    setupDiseaseAdapter(result.data.listStory)
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

    private fun setupDiseaseAdapter(listStoryItem: List<ListStoryItem?>?) {
        val diseaseAdapter = DiseaseAdapter()
        diseaseAdapter.submitList(listStoryItem)
        binding.rvCommonProblems.adapter = diseaseAdapter
    }

    private fun setupAdapterView() {
        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        with(binding) {
            rvCommonProblems.layoutManager = horizontalLayoutManager
            rvCommonProblems.setHasFixedSize(true)
        }

    }

    private fun getWeather() {
        viewModel.getWeather(
            lat = -6.2146,
            lon = 106.8451,
            apiKey = "221e9601799e48d9a11c27d89b9da61a"
        ).observe(requireActivity()) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    val responses = result.data
                    val city = responses.name
                    val weather = responses.weather?.get(0)?.main
                    val temperature = responses.main?.temp?.toInt()
                    val maxTemperature = responses.main?.tempMax?.toInt()
                    val minTemperature = responses.main?.tempMin?.toInt()
                    val icon = responses.weather?.get(0)?.icon
                    val iconUrl = "https://openweathermap.org/img/wn/$icon@2x.png"

                    val celsius = temperature?.minus(273)
                    val max = maxTemperature?.minus(273)
                    val min = minTemperature?.minus(273)

                    val temp = "$celsius°"
                    val maxTemp = "Max Temp: $max °C"
                    val minTemp = "Min Temp: $min °C"

                    with(binding){
                        tvTemp.text = temp
                        tvMaxTemp.text = maxTemp
                        tvMinTemp.text = minTemp
                        tvWeatherStatus.text = weather
                        tvWeatherCity.text = city
                        Glide.with(requireContext())
                            .load(iconUrl)
                            .into(ivWeatherIcon)
                    }

                    Log.d("CUACA", responses.toString())
                    showLoading(false)
                }

                is Result.Error -> {

                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val TAG = "HomeFragment"
    }
}