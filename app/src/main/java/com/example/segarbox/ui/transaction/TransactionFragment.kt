package com.example.segarbox.ui.transaction

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.core.data.Resource
import com.example.core.utils.tokenFormat
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentTransactionBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.login.LoginActivity
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private val viewModel: TransactionViewModel by viewModels()

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
        viewModel.getToken().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
                if (token.isEmpty()) {
                    startActivity(Intent(requireActivity(), LoginActivity::class.java))
                    requireActivity().onBackPressed()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // Update Badge
        if (token.isNotEmpty()) {
            viewModel.getCart(token.tokenFormat()).observe(viewLifecycleOwner) { event ->
                event.getContentIfNotHandled()?.let { resource ->
                    when(resource) {
                        is Resource.Loading -> {
                            viewModel.setLoading(true)
                        }

                        is Resource.Success -> {
                            resource.data?.let { listData ->
                                viewModel.setLoading(false)
                                listData[0].total?.let { total ->
                                    binding.toolbar.ivCart.badgeValue = total
                                }
                            }
                        }

                        is Resource.Empty -> {
                            viewModel.setLoading(false)
                            binding.toolbar.ivCart.badgeValue = 0
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