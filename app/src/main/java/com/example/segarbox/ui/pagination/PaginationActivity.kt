package com.example.segarbox.ui.pagination

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.core.data.Resource
import com.example.core.ui.PaginationAdapter
import com.example.core.utils.*
import com.example.segarbox.R
import com.example.segarbox.databinding.ActivityPaginationBinding
import com.example.segarbox.ui.cart.CartActivity
import com.example.segarbox.ui.detail.DetailActivity
import com.google.android.material.R.attr.colorPrimary
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PaginationActivity : AppCompatActivity(), PaginationAdapter.OnItemPaginationClickCallback,
    View.OnClickListener {
    private var _binding: ActivityPaginationBinding? = null
    private val binding get() = _binding!!
    private var token = ""
    private var filter: String? = null
    private var filterValue: String? = null
    private var isHomeSearchBarPressed: Boolean = false
    private val paginationAdapter = PaginationAdapter(this)
    private val viewModel: PaginationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPaginationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        getPaginationIntent()
        setToolbar()
        setAdapter()
        observeData()
        scrollToTopListAdapter()
        binding.toolbar.ivCart.setOnClickListener(this)
    }

    @Suppress("DEPRECATION")
    private fun setToolbar() {
        setSearchBar(this.getColorStateListPrimary(), this.getColorFromAttr(colorPrimary))

        if (isHomeSearchBarPressed) {
            binding.toolbar.etSearch.requestFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    private fun setSearchBar(colorStroke: ColorStateList, colorIcon: Int) {
        binding.toolbar.tiSearch.apply {
            setBoxStrokeColorStateList(colorStroke)
            defaultHintTextColor = colorStroke
            hintTextColor = colorStroke
            editText!!.setTextColor(colorIcon)

            var searchIcon =
                this@PaginationActivity.getHelperDrawable(R.drawable.ic_baseline_search_24)
            searchIcon = DrawableCompat.wrap(searchIcon)
            DrawableCompat.setTint(searchIcon, colorIcon)
            DrawableCompat.setTintMode(searchIcon, PorterDuff.Mode.SRC_IN)
            editText!!.setCompoundDrawablesWithIntrinsicBounds(searchIcon, null, null, null)

            binding.toolbar.ivCart.setColorFilter(colorIcon, PorterDuff.Mode.SRC_IN)
        }

        binding.toolbar.root.setOnClickListener {
            binding.toolbar.etSearch.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.toolbar.etSearch.windowToken, 0)
        }

        binding.root.setOnClickListener {
            binding.toolbar.etSearch.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.toolbar.etSearch.windowToken, 0)
        }
    }

    private fun getPaginationIntent() {
        filter = intent.getStringExtra(Code.KEY_FILTER)
        filterValue = intent.getStringExtra(Code.KEY_FILTER_VALUE)
        isHomeSearchBarPressed = intent.getBooleanExtra(Code.IS_SEARCH_BAR_PRESSED, false)
    }

    private fun setAdapter() {
        binding.rvPagination.apply {
            val margin = 16
            addItemDecoration(MarginGridItemDecoration(margin.toPixel(this@PaginationActivity)))
            adapter = paginationAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { paginationAdapter.retry() }
            )
        }
    }

    private fun scrollToTopListAdapter() {
        paginationAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onChanged() {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {}

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {}

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                binding.rvPagination.scrollToPosition(0)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {}
        })
    }


    private fun observeData() {
        filter?.let { filter ->
            filterValue?.let { filterValue ->
                viewModel.getProductPaging(filter = filter, filterValue = filterValue)
            }
        }

        var initialSearch = ""
        binding.toolbar.etSearch.addTextChangedListener {
            val searchText = it.toString().trim()

            // if trim
            if (searchText == initialSearch) {
                return@addTextChangedListener
            }

            initialSearch = searchText

            viewModel.viewModelScope.launch {
                delay(300L)
                if (searchText != initialSearch || searchText.isEmpty()) {
                    return@launch
                }
                viewModel.getProductPaging(filter = Code.LABEL_FILTER, filterValue = it.toString())
            }
        }

        viewModel.getProductPagingResponse.observe(this) { event ->
            event.getContentIfNotHandled()?.let { data ->
                paginationAdapter.submitData(lifecycle, data)
            }
        }

        viewModel.getToken().observe(this) { event ->
            event.getContentIfNotHandled()?.let {
                this.token = it
            }
        }

        viewModel.getCartResponse.observe(this) { event ->
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

        viewModel.isLoading.observe(this) { event ->
            event.getContentIfNotHandled()?.let { isLoading ->
                binding.progressBar.isVisible = isLoading
            }
        }


    }

    override fun onResume() {
        super.onResume()
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

    override fun onItemPaginationClicked(productId: Int) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, productId)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.iv_cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
        }
    }

}