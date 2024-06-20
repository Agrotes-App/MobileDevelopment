package com.example.agrotes_mobile.ui.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.agrotes_mobile.R
import com.example.agrotes_mobile.data.remote.responses.disease.DiseaseResponses
import com.example.agrotes_mobile.databinding.FragmentHomeBinding
import com.example.agrotes_mobile.ui.activities.diseaseOption.DiseaseOptionActivity
import com.example.agrotes_mobile.ui.adapter.DiseaseAdapter
import com.example.agrotes_mobile.utils.helper.Result
import com.example.agrotes_mobile.utils.modelFactory.ViewModelFactory
import com.example.agrotes_mobile.utils.modelFactory.WeatherViewModelFactory
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView
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
    private val weatherViewModel: WeatherViewModel by viewModels {
        WeatherViewModelFactory.getInstance()
    }

    // Location Permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> getLocation()
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> getLocation()
                else -> showToast(getString(R.string.permission_denied))
            }
        }

    // check permission
    private fun checkPermission(permission: String): Boolean = ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // check permission
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            showWeather(true)
            getLocation()
        } else {
            showWeather(false)
        }

        showTapTarget()
        setupAction()
        setupCommonProblems()
        setupAdapterView()
    }

    private fun setupAction() {
        with(binding) {
            fabScan.setOnClickListener { toDiseaseOptionActivity() }
            btnScan.setOnClickListener { toDiseaseOptionActivity() }
            btnLocationPermission.setOnClickListener { getLocation() }
        }
    }

    private fun showTapTarget() {
        val sharedPreferences = requireActivity().getSharedPreferences("app_intro", Context.MODE_PRIVATE)
        val isFirstInstall = sharedPreferences.getBoolean("isFirstInstall", true)

        if (isFirstInstall) {
            TapTargetView.showFor(requireActivity(),
                TapTarget.forView(binding.fabScan,
                    getString(R.string.title_tap_target), getString(R.string.message_tap_target))
                    .targetCircleColor(R.color.white),
                object : TapTargetView.Listener() {
                    override fun onTargetClick(view: TapTargetView) {
                        super.onTargetClick(view)
                        toDiseaseOptionActivity()
                        // Set isFirstInstall to false after showing the intro
                        sharedPreferences.edit().putBoolean("isFirstInstall", false).apply()
                    }
                })
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
                    showToast(result.error)
                }
            }

        }
    }

    private fun toDiseaseOptionActivity() {
        val intent = Intent(requireContext(), DiseaseOptionActivity::class.java)
        startActivity(intent)
    }

    private fun setupDiseaseAdapter(listDiseaseItem: List<DiseaseResponses>) {
        val diseaseAdapter = DiseaseAdapter()
        diseaseAdapter.submitList(listDiseaseItem)
        binding.rvCommonProblems.adapter = diseaseAdapter
    }

    private fun setupAdapterView() {
        val horizontalLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        with(binding) {
            rvCommonProblems.layoutManager = horizontalLayoutManager
            rvCommonProblems.setHasFixedSize(true)
        }
    }

    private fun getLocation() {
        // Get current location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        // get latitude and longitude
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude

                    showWeather(true)
                    getWeather(lat, lon)
                } else {
                    showToast(getString(R.string.error_location))
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("StringFormatMatches")
    private fun getWeather(lat: Double?, lon: Double?) {
        weatherViewModel.getWeather(lat, lon).observe(viewLifecycleOwner) { result ->
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

                    val temp = getString(R.string.temperature, celsius)
                    val maxTemp = getString(R.string.max_temperature, max)
                    val minTemp = getString(R.string.min_temperature, min)

                    with(binding) {
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
                    showLoading(false)
                    showToast(result.error)
                }
            }
        }
    }

    private fun showWeather(show: Boolean) {
        with(binding) {
            val viewsToHide = listOf(btnLocationPermission, cardLocationPermission, tvLocationPermission)
            for (view in viewsToHide) view.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    private fun showToast(message: String?) =
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}