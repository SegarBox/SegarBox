package com.example.segarbox.ui.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.example.segarbox.BuildConfig
import com.example.segarbox.R
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityPaginationBinding
import com.example.segarbox.helper.getColorFromAttr
import com.example.segarbox.helper.getColorStateListPrimary
import com.example.segarbox.helper.getHelperDrawable
import com.example.segarbox.helper.toPixel
import com.example.segarbox.ui.adapter.LoadingStateAdapter
import com.example.segarbox.ui.adapter.MarginGridItemDecoration
import com.example.segarbox.ui.adapter.PaginationAdapter
import com.example.segarbox.ui.viewmodel.PaginationViewModel
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory
import com.google.android.material.R.attr.colorPrimary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaginationActivity : AppCompatActivity(), PaginationAdapter.OnItemPaginationClickCallback {

    private var _binding: ActivityPaginationBinding? = null
    private val binding get() = _binding!!
    private var filter: String? = null
    private var filterValue: String? = null
    private var isHomeSearchBarPressed: Boolean = false
    private val paginationAdapter = PaginationAdapter(this)
    private val paginationViewModel by viewModels<PaginationViewModel> {
        RetrofitViewModelFactory.getInstance(RetrofitRepository())
    }

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
        val apiServices = ApiConfig.getApiServices(BuildConfig.BASE_URL_SEGARBOX)

        filter?.let { filter ->
            filterValue?.let { filterValue ->
                paginationViewModel.getProductPaging(
                    apiServices = apiServices,
                    filter = filter,
                    filterValue = filterValue
                ).observe(this) {
                    paginationAdapter.submitData(lifecycle, it)
                }
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

            paginationViewModel.viewModelScope.launch {
                delay(300L)
                if (searchText != initialSearch || searchText.isEmpty()) {
                    return@launch
                }

                paginationViewModel.getProductPaging(
                    apiServices = apiServices,
                    filter = Code.LABEL_FILTER,
                    filterValue = it.toString()
                ).observe(this@PaginationActivity) {
                    paginationAdapter.submitData(lifecycle, it)
                }
            }

        }


    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onItemPaginationClicked(item: ProductItem) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra(Code.KEY_DETAIL_VALUE, item)
        startActivity(intent)
    }

}