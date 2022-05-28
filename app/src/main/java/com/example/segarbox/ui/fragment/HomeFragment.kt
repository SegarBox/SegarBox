package com.example.segarbox.ui.fragment

import android.R.attr.state_focused
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.NestedScrollView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import com.example.segarbox.databinding.FragmentHomeBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.helper.getHelperDrawable
import com.example.segarbox.ui.activity.*
import com.example.segarbox.ui.adapter.AllProductAdapter
import com.example.segarbox.ui.adapter.MarginGridItemDecoration
import com.example.segarbox.ui.adapter.MarginItemDecoration
import com.example.segarbox.ui.adapter.StartShoppingAdapter
import com.example.segarbox.ui.viewmodel.MainViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitRoomViewModelFactory
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSecondaryVariant
import kotlin.math.max
import kotlin.math.min

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F
    private var isThemeDarkMode = false
    private val allProductAdapter = AllProductAdapter()
    private val startShoppingAdapter = StartShoppingAdapter()
    private var checkedChips = ""
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
    }
    private val mainViewModel by viewModels<MainViewModel> {
        RetrofitRoomViewModelFactory.getInstance(RoomRepository(requireActivity().application),
            RetrofitRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        setToolbar()
        setAdapter()
        observeData()
        scrollToTopListAdapter()
        binding.content.btnDetail.setOnClickListener(this)
        binding.content.btnDarkmode.setOnClickListener(this)
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.content.btnCheckout.setOnClickListener(this)
        binding.content.btnInvoice.setOnClickListener(this)
        binding.content.btnRating.setOnClickListener(this)
    }

    private fun setToolbar() {
        // Hooks
        val colorStrokeBelowRatio = ColorStateList(
            arrayOf(
                intArrayOf(-state_focused),
                intArrayOf(state_focused),
            ),

            intArrayOf(
                requireActivity().getColorFromAttr(colorSecondaryVariant),
                requireActivity().getColorFromAttr(colorSecondaryVariant)
            )
        )

        val colorStrokeAboveRatio = ColorStateList(
            arrayOf(
                intArrayOf(-state_focused),
                intArrayOf(state_focused),
            ),

            intArrayOf(
                requireActivity().getColorFromAttr(colorPrimary),
                requireActivity().getColorFromAttr(colorPrimary)
            )
        )

        // Initial Set Toolbar
        binding.toolbar.root.background.alpha = 0
        setSearchBar(colorStrokeBelowRatio,
            requireActivity().getColorFromAttr(colorSecondaryVariant))


        // On Scrolled

        binding.content.scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->
            val headerHeight: Int = binding.content.header.height - binding.toolbar.root.height
            ratio = min(max(scrollY, 0), headerHeight).toFloat() / headerHeight
            val newAlpha = (ratio * 255).toInt()

            // Set Toolbar
            binding.toolbar.root.background.alpha = newAlpha

            // Set Toolbar Items
            if (ratio >= 0.65F) {
                setSearchBar(colorStrokeAboveRatio,
                    requireActivity().getColorFromAttr(colorPrimary))
            } else {
                setSearchBar(colorStrokeBelowRatio,
                    requireActivity().getColorFromAttr(colorSecondaryVariant))
            }

        })
    }

    private fun setSearchBar(colorStroke: ColorStateList, colorIcon: Int) {
        binding.toolbar.tiSearch.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
            editText!!.setTextColor(colorIcon)

            var searchIcon = requireActivity().getHelperDrawable(R.drawable.ic_baseline_search_24)
            searchIcon = DrawableCompat.wrap(searchIcon)
            DrawableCompat.setTint(searchIcon, colorIcon)
            DrawableCompat.setTintMode(searchIcon, PorterDuff.Mode.SRC_IN)
            editText!!.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)

            binding.toolbar.ivCart.setColorFilter(colorIcon, PorterDuff.Mode.SRC_IN)

        }
    }

    private fun scrollToTopListAdapter() {
        startShoppingAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.content.rvStartShopping.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {

            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    private fun observeData() {
        prefViewModel.getTheme().observe(viewLifecycleOwner) { isDarkMode ->
            when {
                isDarkMode -> {
                    isThemeDarkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    binding.content.btnDarkmode.text = "Disable Dark Mode"
                }

                else -> {
                    isThemeDarkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    binding.content.btnDarkmode.text = "Enable Dark Mode"
                }
            }
        }


        mainViewModel.allProductResponse.observe(viewLifecycleOwner) {
            allProductAdapter.submitList(it.data)
        }


        binding.content.chipMostPopular.setOnClickListener {
            mainViewModel.getLabelProduct(1, 10, "bayam")
            mainViewModel.saveCheckedChips("Most")
        }

        binding.content.chipVeggies.setOnClickListener {
            mainViewModel.getCategoryProduct(1, 10, "veggies")
            mainViewModel.saveCheckedChips("veggies")
        }

        binding.content.chipFruits.setOnClickListener {
            mainViewModel.getCategoryProduct(1, 10, "fruits")
            mainViewModel.saveCheckedChips("fruits")
        }

        mainViewModel.checkedChips.observe(viewLifecycleOwner) {
            checkedChips = it
        }

        mainViewModel.productResponse.observe(viewLifecycleOwner) {
            val listProduct = arrayListOf<ProductItem>()
            var dummy: ProductItem? = null
            if (checkedChips == "fruits") {
                dummy = ProductItem(1, 1, 1, it.data.size, "a", "a", "dummyFruits")
            }
            if (checkedChips == "veggies") {
                dummy = ProductItem(1, 1, 1, it.data.size, "a", "a", "dummyVeggies")
            }
            listProduct.addAll(it.data)
            dummy?.let {
                listProduct.add(dummy)
            }
            startShoppingAdapter.submitList(listProduct)
        }

    }

    private fun setAdapter() {

        binding.content.rvStartShopping.apply {
            adapter = startShoppingAdapter
            addItemDecoration(MarginItemDecoration(48))
        }

        binding.content.rvAllProducts.apply {
            adapter = allProductAdapter
            addItemDecoration(MarginGridItemDecoration(48))
        }
    }


    override fun onResume() {
        super.onResume()
        val newAlpha = (ratio * 255).toInt()
        binding.toolbar.root.background.alpha = newAlpha
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_detail -> {
                startActivity(Intent(requireContext(), DetailActivity::class.java))
            }

            R.id.btn_darkmode -> {
                when {
                    isThemeDarkMode -> prefViewModel.saveTheme(false)
                    else -> prefViewModel.saveTheme(true)
                }
            }

            R.id.iv_cart -> {
                startActivity(Intent(requireContext(), CartActivity::class.java))
            }

            R.id.btn_checkout -> {
                startActivity(Intent(requireContext(), CheckoutActivity::class.java))
            }

            R.id.btn_invoice -> {
                startActivity(Intent(requireContext(), InvoiceActivity::class.java))
            }

            R.id.btn_rating -> {
                startActivity(Intent(requireContext(), RatingActivity::class.java))
            }

        }
    }

}