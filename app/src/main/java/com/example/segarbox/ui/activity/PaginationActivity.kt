package com.example.segarbox.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.segarbox.BuildConfig
import com.example.segarbox.R
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.api.ApiConfig
import com.example.segarbox.data.repository.RetrofitRepository
import com.example.segarbox.databinding.ActivityPaginationBinding
import com.example.segarbox.helper.toPixel
import com.example.segarbox.ui.adapter.AllProductAdapter
import com.example.segarbox.ui.adapter.LoadingStateAdapter
import com.example.segarbox.ui.adapter.MarginGridItemDecoration
import com.example.segarbox.ui.adapter.PaginationAdapter
import com.example.segarbox.ui.viewmodel.PaginationViewModel
import com.example.segarbox.ui.viewmodel.RetrofitViewModelFactory

class PaginationActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityPaginationBinding? = null
    private val binding get() = _binding!!
    private var filter: String? = null
    private var filterValue: String? = null
    private var isHomeSearchBarPressed: Boolean = false
    private val paginationAdapter = PaginationAdapter()
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

        Toast.makeText(this, isHomeSearchBarPressed.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setToolbar() {

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


    private fun observeData() {

        filter?.let { filter ->
            filterValue?.let { filterValue ->
                val apiServices = ApiConfig.getApiServices(BuildConfig.BASE_URL_SEGARBOX)
                val database = MainDatabase.getDatabase(application)

                paginationViewModel.getProductPaging(
                    apiServices = apiServices,
                    database = database,
                    filter = filter,
                    filterValue = filterValue
                ).observe(this) {
                    paginationAdapter.submitData(lifecycle, it)
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClick(v: View?) {
        when(v?.id) {
        }
    }
}