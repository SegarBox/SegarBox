package com.example.segarbox.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.viewModels
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.FragmentProfileBinding
import com.example.segarbox.ui.activity.CartActivity
import com.example.segarbox.ui.activity.LoginActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setToolbar()
        observeData()
        darkModeSetting()
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivCart.isVisible = true
            tvTitle.text = "Profile"
        }
    }

    private fun observeData() {
        prefViewModel.getToken().observe(viewLifecycleOwner) { token ->

            Toast.makeText(requireContext(), "token : $token", Toast.LENGTH_SHORT).show()
            if (token.isEmpty()) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().onBackPressed()
            }
        }
    }

    private fun darkModeSetting(){
        prefViewModel.getTheme().observe(viewLifecycleOwner) { isDarkMode:Boolean ->
            when {
                isDarkMode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.content.sDarkMode.isChecked = true
//                    binding.content.darkMode.text = "Light Mode"
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.content.sDarkMode.isChecked = false
                }
            }

            binding.content.sDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                prefViewModel.saveTheme(isChecked)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.iv_cart -> {
                startActivity(Intent(requireContext(), CartActivity::class.java))
            }
        }
    }

}