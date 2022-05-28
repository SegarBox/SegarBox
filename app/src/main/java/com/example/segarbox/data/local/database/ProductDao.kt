package com.example.segarbox.data.local.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.segarbox.data.remote.response.ProductItem

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: List<ProductItem>)

    @Query("SELECT * FROM product")
    fun getAllProduct(): PagingSource<Int, ProductItem>

    @Query("DELETE FROM product")
    suspend fun deleteAll()
}