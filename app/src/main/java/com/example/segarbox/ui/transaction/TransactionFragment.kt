package com.example.segarbox.ui.transaction

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.source.local.datastore.SettingPreferences
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentTransactionBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.login.LoginActivity
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore by preferencesDataStore(name = "settings")

class TransactionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }
    private val transactionViewModel by viewModels<TransactionViewModel> {
        RetrofitViewModelFactory.getInstance(com.example.core.data.RetrofitRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTransactionBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        observeData()
        setTabWithViewPager()
        setToolbar()
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = getString(R.string.transactions)
            ivCart.isVisible = true
        }
    }

    private fun setTabWithViewPager() {
        val sectionsPagerAdapter = TransactionPagerAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        val viewPager = binding.content.viewPager
        val tabLayout = binding.content.tabLayout

        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun observeData() {
        prefViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
            if (token.isEmpty()) {
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().onBackPressed()
            }
        }

        transactionViewModel.userCart.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { userCartResponse ->
                userCartResponse.meta?.let {
                    binding.toolbar.ivCart.badgeValue = it.total
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
        // Update Badge
        if (token.isNotEmpty()) {
            transactionViewModel.getUserCart(token.tokenFormat())
        } else {
            binding.toolbar.ivCart.badgeValue = 0
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

    companion object {
        @StringRes
        val TAB_TITLES = intArrayOf(
            R.string.tab_history_text_1,
            R.string.tab_history_text_2
        )
    }

}