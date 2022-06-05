package com.example.segarbox.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.FragmentProfileBinding
import com.example.segarbox.helper.tokenFormat
import com.example.segarbox.ui.activity.CartActivity
import com.example.segarbox.ui.activity.LoginActivity
import com.example.segarbox.ui.activity.RatingActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.ProfileViewModel
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

private val Context.dataStore by preferencesDataStore(name = "settings")
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }
    private val profileViewModel by viewModels<ProfileViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
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
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.content.btnLogout.setOnClickListener(this)
        binding.content.clRating.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            ivCart.isVisible = true
            tvTitle.text = "Profile"
        }
    }

    private fun observeData() {
        prefViewModel.getTheme().observe(viewLifecycleOwner) { isDarkMode:Boolean ->
            when {
                isDarkMode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.content.sDarkMode.isChecked = true
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

        prefViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
            if (token.isEmpty()) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().onBackPressed()
            }
            else {
                profileViewModel.getUser(token.tokenFormat())
            }
        }

        profileViewModel.userResponse.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { user ->
                binding.content.tvUserName.text = user.data?.name.toString()
                binding.content.tvPhone.text = user.data?.phone.toString()
                binding.content.tvEmail.text = user.data?.email.toString()
            }
        }

        profileViewModel.userCart.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { userCartResponse ->
                userCartResponse.meta?.let {
                    binding.toolbar.ivCart.badgeValue = it.total
                }
            }
        }


        profileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    override fun onResume() {
        super.onResume()
        if (token.isNotEmpty()) {
            profileViewModel.getUserCart(token.tokenFormat())
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
            R.id.btn_logout -> {
                prefViewModel.logout()
                if (token.isNotEmpty()) {
                    profileViewModel.logout(token)
                }
                requireActivity().onBackPressed()
            }
            R.id.cl_rating -> {
                startActivity(Intent(requireContext(), RatingActivity::class.java))
            }
        }
    }

}