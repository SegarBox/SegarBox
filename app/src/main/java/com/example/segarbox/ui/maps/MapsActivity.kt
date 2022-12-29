package com.example.segarbox.ui.maps

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.core.data.Resource
import com.example.core.domain.model.AddressModel
import com.example.core.domain.model.Maps
import com.example.core.utils.Code
import com.example.core.utils.formatted
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!
    private val markerOptions = MarkerOptions()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var addressModel = AddressModel()
    private var token = ""
    private val viewModel: MapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        init()
    }

    private fun init() {
        setToolbar()
        binding.toolbar.ivBack.setOnClickListener(this)
        binding.btnSaveLocation.setOnClickListener(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setPadding(0, 0, 0, 220)
        mMap.uiSettings.isZoomControlsEnabled = true
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setMapStyle()
        getMyLocation()
        observeData()
    }

    private fun setMapStyle() {
        try {
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        } catch (exception: Resources.NotFoundException) {
        }
    }

    private fun placeMarker(latLng: LatLng) {
        mMap.clear()
        mMap.addMarker(
            markerOptions
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    private fun observeData() {

        viewModel.getLatLng.observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                placeMarker(it)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15F))
            }
        }

        viewModel.getAddressResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            viewModel.setLoading(false)
                            addressModel = AddressModel(
                                street = getAddressFromResponse(it),
                                city = getCityFromResponse(it),
                                postalCode = getPostalCodeFromResponse(it)
                            )
                            binding.toolbar.tvTitle.apply {
                                text = getAddressFromResponse(it)
                                textSize = 14F
                            }
                        }
                    }

                    else -> {
                        resource.message?.let {
                            viewModel.setLoading(false)
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                        }
                    }
                }
            }
        }

        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
            }
        }

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }

    }

    private fun getAddressFromResponse(maps: List<Maps>): String =
        maps[0].formattedAddress

    private fun getCityFromResponse(maps: List<Maps>): String {
        for (result in maps) {
            for (addressComponent in result.addressComponents) {
                if (addressComponent.types.contains("administrative_area_level_2") &&
                    (addressComponent.shortName.contains("Kota") ||
                            addressComponent.shortName.contains("Kabupaten"))
                ) {
                    return addressComponent.shortName
                }
            }
        }
        return Code.LOCATION_NOT_FOUND
    }

    private fun getPostalCodeFromResponse(maps: List<Maps>): String {
        for (result in maps) {
            for (addressComponent in result.addressComponents) {
                if (addressComponent.types.contains("postal_code")) {
                    return addressComponent.shortName
                }
            }
        }
        return Code.LOCATION_NOT_FOUND
    }

    private fun setToolbar() {
        binding.toolbar.ivBack.isVisible = true
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            when {
                permissions[ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                permissions[ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLocation()
                }
                else -> {
                    val intent = Intent()
                    intent.putExtra(Code.SNACKBAR_VALUE, getString(R.string.no_location_permission))
                    setResult(Code.RESULT_SNACKBAR, intent)
                    finish()
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(this,
            permission) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLocation() {
        if (checkPermission(ACCESS_FINE_LOCATION) && checkPermission(ACCESS_COARSE_LOCATION)) {
            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

                if (location != null) {
                    viewModel.setLatLng(LatLng(location.latitude, location.longitude))
                    viewModel.getAddress(LatLng(location.latitude,
                        location.longitude).formatted())
                } else {
                    Snackbar.make(binding.root, Code.LOCATION_NOT_FOUND, Snackbar.LENGTH_SHORT)
                        .setAction("OK") {}.show()
                }
            }

            mMap.setOnMapClickListener { latLng ->
                viewModel.setLatLng(latLng)
                viewModel.getAddress(latLng.formatted())
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_back -> finish()

            R.id.btn_save_location -> {
                if (addressModel.street != Code.LOCATION_NOT_FOUND &&
                    addressModel.city != Code.LOCATION_NOT_FOUND &&
                    addressModel.postalCode != Code.LOCATION_NOT_FOUND
                ) {

                    if (token.isNotEmpty()) {
                        viewModel.saveAddress(token.tokenFormat(),
                            addressModel.street,
                            addressModel.city,
                            addressModel.postalCode).observe(this) { event ->
                            event.getContentIfNotHandled()?.let { resource ->
                                when(resource) {
                                    is Resource.Loading -> {
                                        viewModel.setLoading(true)
                                    }

                                    is Resource.Success -> {
                                        resource.data?.let {
                                            viewModel.setLoading(false)
                                            val intent = Intent()
                                            intent.putExtra(Code.SNACKBAR_VALUE, it)
                                            setResult(Code.RESULT_SNACKBAR, intent)
                                            finish()
                                        }
                                    }

                                    else -> {
                                        resource.message?.let {
                                            viewModel.setLoading(false)
                                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                                        }
                                    }
                                }
                            }
                        }
                    }


                } else {
                    Snackbar.make(binding.root,
                        Code.LOCATION_CANT_BE_REACHED,
                        Snackbar.LENGTH_SHORT).setAction("OK") {}.show()
                }
            }
        }
    }
}