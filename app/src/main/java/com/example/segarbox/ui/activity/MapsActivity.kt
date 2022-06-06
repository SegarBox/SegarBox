package com.example.segarbox.ui.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
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
import androidx.datastore.preferences.preferencesDataStore
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.model.AddressModel
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.MapsResponse
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityMapsBinding
import com.example.segarbox.helper.formatted
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.viewmodel.MapsViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
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

private val Context.dataStore by preferencesDataStore(name = "settings")

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!
    private val markerOptions = MarkerOptions()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var addressModel = AddressModel()
    private var token = ""
    private val mapsViewModel by viewModels<MapsViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(dataStore))
    }

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

        mapsViewModel.getLatLng.observe(this) {
            placeMarker(it)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(it, 15F))
        }

        mapsViewModel.address.observe(this) { mapsResponse ->

            addressModel = AddressModel(
                street = getAddressFromResponse(mapsResponse),
                city = getCityFromResponse(mapsResponse),
                postalCode = getPostalCodeFromResponse(mapsResponse)
            )

            binding.toolbar.tvTitle.apply {
                text = getAddressFromResponse(mapsResponse)
                textSize = 14F
            }
        }

        prefViewModel.getToken().observe(this) { token ->
            this.token = token
        }

        mapsViewModel.addressResponse.observe(this) { addressResponse ->
            if (addressResponse.message != null) {
                Snackbar.make(binding.root, addressResponse.message, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
            }
            else {
                addressResponse.info?.let {
                    val intent = Intent()
                    intent.putExtra(Code.SNACKBAR_VALUE, it)
                    setResult(Code.RESULT_SNACKBAR, intent)
                    finish()
                }
            }
        }


        mapsViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }


    }

    private fun getAddressFromResponse(mapsResponse: MapsResponse): String {
        val results = mapsResponse.results

        return if (results != null && results.isNotEmpty()) {
            results[0].formattedAddress
        } else {
            Code.LOCATION_NOT_FOUND
        }
    }

    private fun getCityFromResponse(mapsResponse: MapsResponse): String {
        val results = mapsResponse.results

        if (results != null && results.isNotEmpty()) {
            for (result in results) {
                for (addressComponent in result.addressComponents) {
                    if (addressComponent.types.contains("administrative_area_level_2") &&
                        (addressComponent.shortName.contains("Kota") ||
                                addressComponent.shortName.contains("Kabupaten"))
                    ) {
                        return addressComponent.shortName
                    }
                }
            }

        } else {
            return Code.LOCATION_NOT_FOUND
        }
        return Code.LOCATION_NOT_FOUND
    }

    private fun getPostalCodeFromResponse(mapsResponse: MapsResponse): String {
        val results = mapsResponse.results

        if (results != null && results.isNotEmpty()) {
            for (result in results) {
                for (addressComponent in result.addressComponents) {
                    if (addressComponent.types.contains("postal_code")) {
                        return addressComponent.shortName
                    }
                }
            }

        } else {
            return Code.LOCATION_NOT_FOUND
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
                    mapsViewModel.setLatLng(LatLng(location.latitude, location.longitude))
                    mapsViewModel.getAddress(LatLng(location.latitude,
                        location.longitude).formatted())
                } else {
                    Snackbar.make(binding.root, Code.LOCATION_NOT_FOUND, Snackbar.LENGTH_SHORT)
                        .setAction("OK"){}.show()
                }
            }

            mMap.setOnMapClickListener { latLng ->
                mapsViewModel.setLatLng(latLng)
                mapsViewModel.getAddress(latLng.formatted())
            }

        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    ACCESS_FINE_LOCATION,
                    ACCESS_COARSE_LOCATION
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
                        mapsViewModel.saveAddress(token.tokenFormat(),
                            addressModel.street,
                            addressModel.city,
                            addressModel.postalCode)
                    }


                } else {
                    Snackbar.make(binding.root,
                        Code.LOCATION_CANT_BE_REACHED,
                        Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                }
            }
        }
    }
}