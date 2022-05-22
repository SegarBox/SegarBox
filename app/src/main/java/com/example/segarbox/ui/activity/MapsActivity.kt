package com.example.segarbox.ui.activity

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMapsBinding
import com.example.segarbox.ui.viewmodel.MapsViewModel
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

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, View.OnClickListener {

    private lateinit var mMap: GoogleMap
    private var _binding: ActivityMapsBinding? = null
    private val binding get() = _binding!!
    private val markerOptions = MarkerOptions()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val mapsViewModel by viewModels<MapsViewModel>()

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

        mapsViewModel.getAddress.observe(this) {
            binding.toolbar.tvTitle.apply {
                text = it.address
                textSize = 14F
            }
        }

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
                    Toast.makeText(this,
                        getString(R.string.no_location_permission),
                        Toast.LENGTH_SHORT).show()
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
                    mapsViewModel.setAddress(this, LatLng(location.latitude, location.longitude))
                } else {
                    Toast.makeText(this,
                        getString(R.string.location_not_found),
                        Toast.LENGTH_SHORT).show()
                }
            }

            mMap.setOnMapClickListener { latLng ->
                mapsViewModel.setAddress(this, latLng)
                mapsViewModel.setLatLng(latLng)
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
        }
    }
}