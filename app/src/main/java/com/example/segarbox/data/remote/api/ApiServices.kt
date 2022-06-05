package com.example.segarbox.data.remote.api

import com.example.segarbox.BuildConfig
import com.example.segarbox.data.local.model.MakeOrderBody
import com.example.segarbox.data.local.static.Code
import com.example.segarbox.data.remote.response.*
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {

    @GET("json")
    suspend fun getAddress(
        @Query("latlng") latLng: String,
        @Query("key") apiKey: String = BuildConfig.GOOGLE_MAPS_KEY,
    ): Response<MapsResponse>

    @GET("city")
    suspend fun getCity(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY,
    ): Response<CityResponse>

    @FormUrlEncoded
    @POST("cost")
    suspend fun getShippingCosts(
        @Header("key") key: String = BuildConfig.RAJAONGKIR_KEY,
        @Field("origin") origin: String = Code.SEMARANG_ID,
        @Field("destination") destination: String,
        @Field("weight") weight: String,
        @Field("courier") courier: String,
    ): Response<ShippingResponse>

    @FormUrlEncoded
    @POST("register")
    @Headers("Accept: application/json")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String
    ): Response<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    @Headers("Accept: application/json")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET("products")
    @Headers("Accept: application/json")
    suspend fun getAllProduct(
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int,
        @Query("sort") sort: String = "label"
    ): Response<ProductResponse>

    @GET("products/{id}")
    @Headers("Accept: application/json")
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<ProductByIdResponse>

    @GET("products")
    @Headers("Accept: application/json")
    suspend fun getCategoryProduct(
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int,
        @Query("filter[category]") category: String
    ): Response<ProductResponse>

    @GET("products")
    @Headers("Accept: application/json")
    suspend fun getLabelProduct(
        @Query("page[number]") page: Int,
        @Query("page[size]") size: Int,
        @Query("filter[label]") label: String
    ): Response<ProductResponse>

    @GET("users/1")
    @Headers("Accept: application/json")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): Response<UserResponse>

    @FormUrlEncoded
    @POST("logout")
    @Headers("Accept: application/json")
    suspend fun logout(
        @Header("Authorization") token: String
    ): Response<LogoutResponse>

    @FormUrlEncoded
    @POST("carts")
    @Headers("Accept: application/json")
    suspend fun addToCart(
        @Header("Authorization") token: String,
        @Field("product_id") productId: Int,
        @Field("product_qty") productQty: Int,
        @Field("is_checked") isChecked: Int = 1,
    ): Response<AddCartResponse>

    @GET("carts")
    @Headers("Accept: application/json")
    suspend fun getUserCart(
        @Header("Authorization") token: String,
        @Query("include") include: String = "product",
        @Query("page[size]") size: Int = 100,
        @Query("sort") sort: String = "-id"
    ): Response<UserCartResponse>

    @GET("carts")
    @Headers("Accept: application/json")
    suspend fun getIsCheckedUserCart(
        @Header("Authorization") token: String,
        @Query("include") include: String = "product",
        @Query("page[size]") size: Int = 100,
        @Query("sort") sort: String = "-id",
        @Query("filter[is_checked]") isChecked: Int = 1
    ): Response<UserCartResponse>

    @DELETE("carts/{id}")
    @Headers("Accept: application/json")
    suspend fun deleteUserCart(
        @Header("Authorization") token: String,
        @Path("id") cartId: Int
    ): Response<DeleteCartResponse>

    @FormUrlEncoded
    @PUT("carts/{id}")
    @Headers("Accept: application/json")
    suspend fun updateUserCart(
        @Header("Authorization") token: String,
        @Path("id") cartId: Int,
        @Field("product_id") productId: Int,
        @Field("product_qty") productQty: Int,
        @Field("is_checked") isChecked: Int = 1
    ): Response<UpdateCartResponse>


    @FormUrlEncoded
    @POST("carts/details")
    @Headers("Accept: application/json")
    suspend fun getCartDetail(
        @Header("Authorization") token: String,
        @Field("shipping_cost") shippingCost: Int = 0,
    ): Response<CartDetailResponse>

    @FormUrlEncoded
    @POST("addresses")
    @Headers("Accept: application/json")
    suspend fun saveAddress(
        @Header("Authorization") token: String,
        @Field("street") street: String,
        @Field("city") city: String,
        @Field("postal_code") postalCode: String,
    ): Response<AddAddressResponse>

    @GET("addresses")
    @Headers("Accept: application/json")
    suspend fun getUserAddresses(
        @Header("Authorization") token: String,
        @Query("page[size]") size: Int = 100,
        @Query("sort") sort: String = "-id",
    ): Response<GetAddressResponse>


    @DELETE("addresses/{id}")
    @Headers("Accept: application/json")
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("id") addressId: Int
    ): Response<DeleteAddressResponse>


    @POST("transactions")
    @Headers("Accept: application/json")
    suspend fun makeOrderTransaction(
        @Header("Authorization") token: String,
        @Body body: MakeOrderBody
    ): Response<MakeOrderResponse>

    @GET("transactions/{id}")
    @Headers("Accept: application/json")
    suspend fun getTransactionById(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Query("page[size]") size: Int = 100,
        @Query("sort") sort: String = "-id",
        @Query("include") include: String = "productTransactions,address",
    ): Response<TransactionByIdResponse>

    @GET("transactions")
    @Headers("Accept: application/json")
    suspend fun getTransactions(
        @Header("Authorization") token: String,
        @Query("filter[status]") status: String,
        @Query("page[size]") size: Int = 100,
        @Query("sort") sort: String = "-id",
        @Query("include") include: String = "productTransactions,address",
    ): Response<TransactionsResponse>

    @PUT("transactions/{id}")
    @Headers("Accept: application/json")
    suspend fun updateTransactionStatus(
        @Header("Authorization") token: String,
        @Path("id") transactionId: Int
    ): Response<TransactionsStatusResponse>

}
