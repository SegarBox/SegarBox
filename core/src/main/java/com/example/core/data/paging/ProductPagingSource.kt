package com.example.core.data.paging

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.core.data.source.remote.network.SegarBoxApiServices
import com.example.core.data.source.remote.response.ProductItem
import com.example.core.data.source.remote.response.ProductResponse
import com.example.core.utils.Code
import retrofit2.Response

class ProductPagingSource(
    private val segarboxApiServices: SegarBoxApiServices,
    private val filter: String,
    private val filterValue: String,
) : PagingSource<Int, ProductItem>() {

    private lateinit var responseData: Response<ProductResponse>

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductItem> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX

            when (filter) {
                Code.CATEGORY_FILTER -> {
                    responseData = segarboxApiServices.getCategoryProduct(
                        page = position,
                        size = params.loadSize,
                        category = filterValue
                    )
                }
                Code.LABEL_FILTER -> {
                    responseData = segarboxApiServices.getLabelProduct(
                        page = position,
                        size = params.loadSize,
                        label = filterValue
                    )
                }
                else -> {
                    responseData = segarboxApiServices.getAllProduct(
                        page = position,
                        size = params.loadSize,
                    )
                }
            }

            val responseBody = responseData.body()!!

            LoadResult.Page(
                data = responseBody.data,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseBody.data.isEmpty()) null else position + 1
            )
        } catch (ex: Exception) {
            return LoadResult.Error(ex)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ProductItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}