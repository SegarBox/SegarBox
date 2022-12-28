package com.example.segarbox.ui.home

import android.R.attr.state_checked
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.core.data.Resource
import com.example.core.utils.getColorFromAttr
import com.example.core.utils.getHelperColor
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityMainBinding
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        setBottomNav()
        observeData()
    }

    private fun setBottomNav() {
        val bottomNavColor = ColorStateList(
            arrayOf(
                intArrayOf(-state_checked),
                intArrayOf(state_checked),
            ),

            intArrayOf(
                this.getHelperColor(R.color.lightModeUnActiveButton),
                this.getColorFromAttr(colorPrimary)
            )
        )

        binding.bottomNav.itemIconTintList = bottomNavColor

        val navView = binding.bottomNav
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)

    }

    private fun observeData() {
        viewModel.getCityCount().observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            viewModel.setLoading(false)
                            if (it == 0) {
                                viewModel.getCityFromApi()
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

        viewModel.getCityFromApiResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            viewModel.setLoading(false)
                            viewModel.insertCityToDb(it)
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

        viewModel.getIntro().observe(this) { event ->
            event.getContentIfNotHandled()?.let { isAlreadyIntro ->
                if (!isAlreadyIntro) {
                    viewModel.saveIntro(true)
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}