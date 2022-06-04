package com.example.segarbox.data.remote.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class TransactionByIdResponse(

    @field:SerializedName("data")
    val data: TransactionItem? = null,

    @field:SerializedName("message")
    val message: String? = null,
)

data class TransactionItem(

    @field:SerializedName("product_transactions")
    val productTransactions: List<ProductTransactionsItem>,

    @field:SerializedName("qty_transaction")
    val qtyTransaction: Int,

    @field:SerializedName("address")
    val address: AddressItem,

    @field:SerializedName("total_price")
    val totalPrice: Int,

    @field:SerializedName("subtotal_products")
    val subtotalProducts: Int,

    @field:SerializedName("shipping_cost")
    val shippingCost: Int,

    @field:SerializedName("address_id")
    val addressId: Int,

    @field:SerializedName("created_at")
    val createdAt: String,

    @field:SerializedName("updated_at")
    val updatedAt: String,

    @field:SerializedName("user_id")
    val userId: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("invoice_number")
    val invoiceNumber: String,

    @field:SerializedName("status")
    val status: String,
)

@Parcelize
data class ProductTransactionsItem(

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("size")
    val size: Int,

    @field:SerializedName("price")
    val price: Int,

    @field:SerializedName("qty")
    val qty: Int,

    @field:SerializedName("product_qty")
    val productQty: Int,

    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("label")
    val label: String,

    @field:SerializedName("detail")
    val detail: String,

    @field:SerializedName("category")
    val category: String = "veggies",

    ): Parcelable
