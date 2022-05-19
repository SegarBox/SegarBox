package com.example.segarbox.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentTransactionBinding
import com.example.segarbox.ui.activity.CartActivity
import com.example.segarbox.ui.activity.DetailActivity
import com.example.segarbox.ui.adapter.TransactionPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator


class TransactionFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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