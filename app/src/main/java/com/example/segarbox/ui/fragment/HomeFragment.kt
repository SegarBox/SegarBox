package com.example.segarbox.ui.fragment

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
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.R
import com.example.segarbox.data.local.datastore.SettingPreferences
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.data.repository.RoomRepository
import com.example.segarbox.databinding.FragmentHomeBinding
import com.example.segarbox.helper.*
import com.example.segarbox.ui.activity.*
import com.example.segarbox.ui.adapter.AllProductAdapter
import com.example.segarbox.ui.adapter.MarginGridItemDecoration
import com.example.segarbox.ui.adapter.MarginItemDecoration
import com.example.segarbox.ui.adapter.StartShoppingAdapter
import com.example.segarbox.ui.viewmodel.MainViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModel
import com.example.segarbox.ui.viewmodel.PrefViewModelFactory
import com.example.segarbox.ui.viewmodel.RetrofitRoomViewModelFactory
import com.google.android.gms.dynamic.IFragmentWrapper
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSecondaryVariant
import kotlin.math.max
import kotlin.math.min

private val Context.dataStore by preferencesDataStore(name = "settings")
class HomeFragment : Fragment(), View.OnClickListener,
    StartShoppingAdapter.OnItemStartShoppingClickCallback,
    AllProductAdapter.OnItemAllProductClickCallback {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F
    private val allProductAdapter = AllProductAdapter(this)
    private val startShoppingAdapter = StartShoppingAdapter(this)
    private var checkedChips = ""
    private var token = ""
    private val mainViewModel by viewModels<MainViewModel> {
        RetrofitRoomViewModelFactory.getInstance(RoomRepository(requireActivity().application),
            RetrofitRepository())
    }
    private val prefViewModel by viewModels<PrefViewModel> {
        PrefViewModelFactory.getInstance(SettingPreferences.getInstance(requireActivity().dataStore))
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
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.toolbar.etSearch.setOnClickListener(this)
        binding.content.btnCheckout.setOnClickListener(this)
        binding.content.btnInvoice.setOnClickListener(this)
        binding.content.btnRating.setOnClickListener(this)
        binding.content.tvSeeAll.setOnClickListener(this)
    }

    private fun setToolbar() {
        // Initial Set Toolbar
        binding.toolbar.root.background.alpha = 0
        binding.toolbar.etSearch.isFocusable = false
        setSearchBar(requireActivity().getColorStateListSecondaryVariant(),
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
                setSearchBar(requireActivity().getColorStateListPrimary(),
                    requireActivity().getColorFromAttr(colorPrimary))
            } else {
                setSearchBar(requireActivity().getColorStateListSecondaryVariant(),
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
            override fun onChanged() {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                binding.content.rvStartShopping.scrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    private fun observeData() {

        prefViewModel.getToken().observe(viewLifecycleOwner) { token ->
            this.token = token
        }

        mainViewModel.allProductResponse.observe(viewLifecycleOwner) {
            allProductAdapter.submitList(it.data)
        }


        binding.content.chipMostPopular.setOnClickListener {
            mainViewModel.getLabelProduct(1, 10, "bayam")
            mainViewModel.saveCheckedChips(Code.MOST_POPULAR_CHIPS)
        }

        binding.content.chipVeggies.setOnClickListener {
            mainViewModel.getCategoryProduct(1, 10, Code.VEGGIES_CATEGORY)
            mainViewModel.saveCheckedChips(Code.VEGGIES_CHIPS)
        }

        binding.content.chipFruits.setOnClickListener {
            mainViewModel.getCategoryProduct(1, 10, Code.FRUITS_CATEGORY)
            mainViewModel.saveCheckedChips(Code.FRUITS_CHIPS)
        }

        mainViewModel.checkedChips.observe(viewLifecycleOwner) {
            checkedChips = it
        }

        mainViewModel.productResponse.observe(viewLifecycleOwner) {
            val listProduct = arrayListOf<ProductItem>()
            var dummy: ProductItem? = null
            if (checkedChips == Code.FRUITS_CHIPS) {
                dummy = addDummyProduct(Code.DUMMY_FRUITS, it.data.size)
            }
            if (checkedChips == Code.VEGGIES_CHIPS) {
                dummy = addDummyProduct(Code.DUMMY_VEGGIES, it.data.size)
            }
            listProduct.addAll(it.data)
            dummy?.let {
                listProduct.add(dummy)
            }
            startShoppingAdapter.submitList(listProduct)
        }

        mainViewModel.userCart.observe(viewLifecycleOwner) { userCartResponse ->
            userCartResponse.meta?.let {
                binding.toolbar.ivCart.badgeValue = it.total
            }
        }

        mainViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }

    }

    private fun setAdapter() {

        binding.content.rvStartShopping.apply {
            val margin = 16
            adapter = startShoppingAdapter
            addItemDecoration(MarginItemDecoration(margin.toPixel(requireContext())))
        }

        binding.content.rvAllProducts.apply {
            val margin = 16
            adapter = allProductAdapter
            addItemDecoration(MarginGridItemDecoration(margin.toPixel(requireContext())))
        }
    }


    override fun onResume() {
        super.onResume()
        // Update Badge
        if (token.isNotEmpty()) {
            mainViewModel.getUserCart(token.tokenFormat())
        } else {
            binding.toolbar.ivCart.badgeValue = 0
        }

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

            R.id.tv_see_all -> {
                val intent = Intent(requireContext(), PaginationActivity::class.java)
                intent.putExtra(Code.KEY_FILTER, Code.NONE_FILTER)
                intent.putExtra(Code.KEY_FILTER_VALUE, Code.EMPTY_STRING)
                startActivity(intent)
            }

            R.id.et_search -> {
                val intent = Intent(requireContext(), PaginationActivity::class.java)
                intent.putExtra(Code.IS_SEARCH_BAR_PRESSED, true)
                startActivity(intent)
            }

        }
    }

    override fun onItemStartShoppingClicked(item: ProductItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, item)
        startActivity(intent)
    }

    override fun onStartShoppingSeeAllClicked(item: ProductItem) {
        val intent = Intent(requireContext(), PaginationActivity::class.java)

        if (item.category == Code.DUMMY_VEGGIES) {
            intent.putExtra(Code.KEY_FILTER, Code.CATEGORY_FILTER)
            intent.putExtra(Code.KEY_FILTER_VALUE, Code.VEGGIES_CATEGORY)
        }

        if (item.category == Code.DUMMY_FRUITS) {
            intent.putExtra(Code.KEY_FILTER, Code.CATEGORY_FILTER)
            intent.putExtra(Code.KEY_FILTER_VALUE, Code.FRUITS_CATEGORY)
        }

        startActivity(intent)
    }

    override fun onItemAllProductClicked(item: ProductItem) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, item)
        startActivity(intent)
    }

}