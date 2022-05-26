package com.example.segarbox.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.databinding.FragmentTransactionBinding
import com.example.segarbox.ui.activity.CartActivity
import com.example.segarbox.ui.activity.LoginActivity
import com.example.segarbox.ui.adapter.TransactionPagerAdapter
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.google.android.material.tabs.TabLayoutMediator

private val Context.dataStore by preferencesDataStore(name = "settings")

class TransactionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
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
        setToolbar()
        setTabWithViewPager()
        observeData()
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    private fun setToolbar() {
        binding.toolbar.apply {
            tvTitle.text = "Transaction"
            ivCart.isVisible = true
        }
    }

    private fun setTabWithViewPager() {
        val sectionsPagerAdapter = TransactionPagerAdapter(requireActivity())
        val viewPager = binding.content.viewPager
        val tabLayout = binding.content.tabLayout

        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
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