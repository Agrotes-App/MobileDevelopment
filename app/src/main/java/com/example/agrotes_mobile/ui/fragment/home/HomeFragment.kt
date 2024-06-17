package com.example.agrotes_mobile.ui.fragment.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.utils.Result
import com.example.agrotes_mobile.databinding.FragmentHomeBinding
import com.example.agrotes_mobile.ui.adapter.DiseaseAdapter
import com.example.agrotes_mobile.ui.activities.camera.CameraActivity
import com.example.agrotes_mobile.utils.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null
    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    // Location Permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> { getLocation()}
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> { getLocation()}
                else -> { Log.d("Permission", "No Permission")}
            }
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

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)){
            with(binding) {
                cardLocationPermission.alpha = 0f
                tvLocationPermission.alpha = 0f
                btnLocationPermission.alpha = 0f
            }
            getLocation()
        }else{
            with(binding) {
                cardLocationPermission.alpha = 1f
                tvLocationPermission.alpha = 1f
                btnLocationPermission.alpha = 1f
            }
        }

        setupAction()
        setupCommonProblems()
        setupAdapterView()
    }

    private fun setupAction() {
        with(binding) {
            fabScan.setOnClickListener { toCameraActivity() }
            btnScan.setOnClickListener { toCameraActivity() }
            btnLocationPermission.setOnClickListener{ getLocation() }
        }
    }

    private fun setupCommonProblems() {
        viewModel.getAllDisease().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    setupDiseaseAdapter(result.data)
                    showLoading(false)
                }

                is Result.Error -> {
                    showLoading(false)
                    Log.d("ERROR", result.error)
                    showToast(result.error)
                }
            }

        }
    }

    private fun toCameraActivity() {
        val intent = Intent(requireContext(), CameraActivity::class.java)
        startActivity(intent)
    }

    private fun setupDiseaseAdapter(listDiseaseItem: List<DiseaseResponses>) {
        val diseaseAdapter = DiseaseAdapter()
        diseaseAdapter.submitList(listDiseaseItem)
        binding.rvCommonProblems.adapter = diseaseAdapter
    }

    private fun setupAdapterView() {
        val horizontalLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        with(binding) {
            rvCommonProblems.layoutManager = horizontalLayoutManager
            rvCommonProblems.setHasFixedSize(true)
        }

    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getLocation(){
        // Get current location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    with(binding) {
                        cardLocationPermission.alpha = 0f
                        tvLocationPermission.alpha = 0f
                        btnLocationPermission.alpha = 0f
                    }

                    lat = location.latitude
                    lon = location.longitude
                    getWeather(lat, lon)
                } else {
                    Toast.makeText(requireContext(), "Location is not found. Try Again", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    private fun getWeather(lat: Double?, lon: Double?) {
        viewModel.getWeather(lat, lon).observe(requireActivity()) { result ->
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