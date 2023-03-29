package com.example.segarbox.ui.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Resource
import com.example.core.domain.model.Product
import com.example.core.ui.AllProductAdapter
import com.example.core.ui.StartShoppingAdapter
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.FragmentHomeBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.detail.DetailActivity
import com.example.segarbox.ui.pagination.PaginationActivity
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.R.attr.colorSecondaryVariant
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener,
    StartShoppingAdapter.OnItemStartShoppingClickCallback,
    AllProductAdapter.OnItemAllProductClickCallback {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var ratio = 0F
    private val allProductAdapter = AllProductAdapter(this)
    private val startShoppingAdapter = StartShoppingAdapter(this)
    private var token = ""
    private val viewModel: MainViewModel by viewModels()

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
        binding.toolbar.ivCart.setOnClickListener(this)
        binding.toolbar.etSearch.setOnClickListener(this)
        binding.content.tvSeeAll.setOnClickListener(this)
        binding.content.chipMostPopular.setOnClickListener(this)
        binding.content.chipVeggies.setOnClickListener(this)
        binding.content.chipFruits.setOnClickListener(this)
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
                binding.content.rvStartShopping.smoothScrollToPosition(0)
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }

    private fun observeData() {

        viewModel.getToken().observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { token ->
                this.token = token
            }
        }

        viewModel.checkedChips.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { checkedChips ->
                CHIPS_VALUE = checkedChips

                when (checkedChips) {
                    Code.MOST_POPULAR_CHIPS -> {
                        viewModel.getProductByMostPopular()
                    }
                    Code.VEGGIES_CHIPS -> {
                        viewModel.getProductByCategory(1, 10, Code.VEGGIES_CATEGORY)
                    }
                    else -> {
                        viewModel.getProductByCategory(1, 10, Code.FRUITS_CATEGORY)
                    }
                }
            }
        }

        viewModel.getAllProductsResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            allProductAdapter.submitList(it)
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        viewModel.setLoading(false)
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                .setAction("OK") {}.show()
                        }
                    }
                }
            }
        }

        viewModel.getProductResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            val listProduct = arrayListOf<Product>()
                            var dummy: Product? = null
                            if (CHIPS_VALUE == Code.FRUITS_CHIPS) {
                                dummy = addDummyProduct(Code.DUMMY_FRUITS)
                            }
                            if (CHIPS_VALUE == Code.VEGGIES_CHIPS) {
                                dummy = addDummyProduct(Code.DUMMY_VEGGIES)
                            }
                            listProduct.addAll(it)
                            dummy?.let {
                                listProduct.add(dummy)
                            }
                            startShoppingAdapter.submitList(listProduct)
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        viewModel.setLoading(false)
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                .setAction("OK") {}.show()
                            viewModel.setLoading(false)
                        }
                    }
                }

            }
        }

        viewModel.getCartResponse.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        viewModel.setLoading(true)
                    }

                    is Resource.Success -> {
                        resource.data?.let { listCart ->
                            listCart[0].total?.let { total ->
                                binding.toolbar.ivCart.badgeValue = total
                            }
                            viewModel.setLoading(false)
                        }
                    }

                    is Resource.Empty -> {
                        binding.toolbar.ivCart.badgeValue = 0
                        viewModel.setLoading(false)
                    }

                    else -> {
                        resource.message?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT)
                                .setAction("OK") {}.show()
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
        val newAlpha = (ratio * 255).toInt()
        binding.toolbar.root.background.alpha = newAlpha

        if (token.isNotEmpty()) {
            viewModel.getCart(token.tokenFormat())
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

            R.id.chip_most_popular -> {
                viewModel.saveCheckedChips(Code.MOST_POPULAR_CHIPS)
            }

            R.id.chip_veggies -> {
                viewModel.saveCheckedChips(Code.VEGGIES_CHIPS)
            }

            R.id.chip_fruits -> {
                viewModel.saveCheckedChips(Code.FRUITS_CHIPS)
            }

        }
    }

    override fun onItemStartShoppingClicked(productId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, productId)
        startActivity(intent)
    }

    override fun onStartShoppingSeeAllClicked(item: Product) {
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

    override fun onItemAllProductClicked(productId: Int) {
        val intent = Intent(requireContext(), DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, productId)
        startActivity(intent)
    }

}