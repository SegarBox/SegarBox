package com.example.segarbox.ui.paging

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.segarbox.data.local.database.MainDatabase
import com.example.segarbox.data.local.database.RemoteKeys
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.api.ApiServices
import com.example.segarbox.data.remote.response.ProductItem
import com.example.segarbox.data.remote.response.ProductResponse
import retrofit2.Response

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator(
    private val database: MainDatabase,
    private val apiServices: ApiServices,
    private val filter: String,
    private val filterValue: String
) : RemoteMediator<Int, ProductItem>() {

    private lateinit var responseData: Response<ProductResponse>

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductItem>,
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null)
                prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {

            when (filter) {
                Code.CATEGORY_FILTER -> {
                    responseData = apiServices.getCategoryProduct(
                        page = page,
                        size = state.config.pageSize,
                        category = filterValue
                    )
                }
                Code.LABEL_FILTER -> {
                    responseData = apiServices.getLabelProduct(
                        page = page,
                        size = state.config.pageSize,
                        label = filterValue
                    )
                }
                else -> {
                    responseData = apiServices.getAllProduct(
                        page = page,
                        size = state.config.pageSize
                    )
                }
            }

            val responseBody = responseData.body()!!

            val endOfPaginationReached = responseBody.data.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.remoteKeysDao().deleteRemoteKeys()
                    database.productDao().deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = responseBody.data.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.productDao().insertProduct(responseBody.data)
                database.remoteKeysDao().insertAll(keys)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
        }

    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, ProductItem>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, ProductItem>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.remoteKeysDao().getRemoteKeys(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, ProductItem>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.remoteKeysDao().getRemoteKeys(id)
            }
        }
    }

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

}