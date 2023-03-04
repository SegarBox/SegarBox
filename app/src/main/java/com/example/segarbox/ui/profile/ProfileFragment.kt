package com.example.segarbox.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.Resource
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentProfileBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.login.LoginActivity
import com.example.segarbox.ui.rating.RatingActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val viewModel: ProfileViewModel by viewModels()

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
            tvTitle.text = getString(R.string.profile_title)
        }
    }

    private fun observeData() {
        viewModel.getToken().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
                if (token.isNotEmpty()) {
                    viewModel.getCart(token.tokenFormat())
                    viewModel.getUser(token.tokenFormat())
                } else {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().onBackPressed()
                }
            }
        }

        viewModel.getTheme().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isDarkMode ->
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
            }
        }

        binding.content.sDarkMode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveTheme(isChecked)
        }

        viewModel.getUserResponse.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            binding.content.apply {
                                tvUserName.text = it.name
                                tvPhone.text = it.phone
                                tvEmail.text = it.email
                            }
                            viewModel.setLoading(false)
                        }
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                            viewModel.setLoading(false)
                        }
                    }
                }
            }
        }

        viewModel.getCartResponse.observe(viewLifecycleOwner){ event ->
            event.getContentIfNotHandled()?.let { resource ->
                when(resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { listData ->
                            listData[0].total?.let { total ->
                                binding.toolbar.ivCart.badgeValue = total
                            }
                            viewModel.setLoading(false)
                        }
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).setAction("OK"){}.show()
                            viewModel.setLoading(false)
                        }
                    }
                }
            }
        }


        viewModel.isLoading.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
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
            R.id.btn_logout -> {
                viewModel.deleteToken()
                if (token.isNotEmpty()) {
                    viewModel.logout(token)
                }
                requireActivity().onBackPressed()
            }
            R.id.cl_rating -> {
                startActivity(Intent(requireContext(), RatingActivity::class.java))
            }
        }
    }

}