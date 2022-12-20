package com.example.core.utils

import com.example.core.data.source.remote.response.*
import com.example.core.domain.model.*
import com.example.core.domain.model.Shipping
import kotlin.math.cos

object DataMapper {

    fun mapGetAddressResponseToAddresses(getAddressResponse: GetAddressResponse): List<Address>? =
        getAddressResponse.data?.map {
            Address(
                id = it.id,
                userId = it.userId,
                street = it.street,
                city = it.city,
                postalCode = it.postalCode
            )
        }

    fun mapProductItemToProduct(productItem: ProductItem): Product =
        Product(
            id = productItem.id,
            image = productItem.image,
            label = productItem.label,
            size = productItem.size,
            price = productItem.price,
            qty = productItem.qty,
            detail = productItem.detail,
            category = productItem.category
        )

    fun mapCartResponseToCarts(userCartResponse: UserCartResponse): List<Cart>? =
        userCartResponse.data?.map {
            Cart(
                id = it.id,
                userId = it.userId,
                productId = it.productId,
                productQty = it.productQty,
                product = mapProductItemToProduct(it.product),
                isChecked = it.isChecked
            )
        }

    fun mapCartDetailResponseToCartDetail(cartDetailResponse: CartDetailResponse): CartDetail =
        CartDetail(
            qtyTransaction = cartDetailResponse.qtyTransaction,
            totalPrice = cartDetailResponse.totalPrice,
            subtotalProducts = cartDetailResponse.subtotalProducts,
            shippingCost = cartDetailResponse.shippingCost
        )

    fun mapMakeOrderResponseToMakeOrder(makeOrderResponse: MakeOrderResponse): MakeOrder? =
        makeOrderResponse.data?.let {
            MakeOrder(id = it.id)
        }

    fun mapProductByIdResponseToProduct(productByIdResponse: ProductByIdResponse): Product? =
        productByIdResponse.data?.let {
            Product(
                id = it.id,
                image = it.image,
                label = it.label,
                size = it.size,
                price = it.price,
                qty = it.qty,
                detail = it.detail,
                category = it.category
            )
        }

    fun mapCityResponseToCities(cityResponse: CityResponse): List<City> =
        cityResponse.rajaongkir.results.map {
            City(
                cityId = it.cityId,
                cityName = it.cityName,
                provinceId = it.provinceId,
                province = it.province,
                type = it.type,
                postalCode = it.postalCode
            )
        }

    fun mapProductResponseToProducts(productResponse: ProductResponse): List<Product> =
        productResponse.data.map {
            Product(
                id = it.id,
                image = it.image,
                label = it.label,
                size = it.size,
                price = it.price,
                qty = it.qty,
                detail = it.detail,
                category = it.category
            )
        }

    fun mapProductTransactionItemToTransactionProduct(productTransactionsItem: ProductTransactionsItem): TransactionProduct =
        TransactionProduct(
            image = productTransactionsItem.image,
            size = productTransactionsItem.size,
            price = productTransactionsItem.price,
            qty = productTransactionsItem.qty,
            productQty = productTransactionsItem.productQty,
            id = productTransactionsItem.id,
            productId = productTransactionsItem.productId,
            label = productTransactionsItem.label,
            detail = productTransactionsItem.detail,
            category = productTransactionsItem.category
        )

    fun mapTransactionByIdResponseToTransaction(transactionByIdResponse: TransactionByIdResponse): Transaction? =
        transactionByIdResponse.data?.let {
            Transaction(
                productTransactions = it.productTransactions.map { productTransactionItem ->
                    mapProductTransactionItemToTransactionProduct(productTransactionItem)
                },
                qtyTransaction = it.qtyTransaction,
                address = it.address,
                totalPrice = it.totalPrice,
                subtotalProducts = it.subtotalProducts,
                shippingCost = it.shippingCost,
                addressId = it.addressId,
                userId = it.userId,
                id = it.id,
                invoiceNumber = it.invoiceNumber,
                status = it.status
            )
        }

    fun mapUserResponseToUser(userResponse: UserResponse): User? =
        userResponse.data?.let {
            User(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone
            )
        }

    private fun mapUserItemToUser(userItem: UserItem?): User? =
        userItem?.let {
            User(
                id = it.id,
                name = it.name,
                email = it.email,
                phone = it.phone
            )
        }

    private fun mapErrorsItemToLoginError(errorsItem: ErrorsItem?): LoginError? =
        errorsItem?.let {
            LoginError(
                password = it.password,
                email = it.email
            )
        }

    private fun mapErrorsItemToRegisterError(errors: Errors?): RegisterError? =
        errors?.let {
            RegisterError(
                password = it.password,
                email = it.email,
                name = it.name,
                phone = it.phone
            )
        }

    fun mapLoginResponseToLogin(loginResponse: LoginResponse): Login =
        Login(
            token = loginResponse.token,
            user = mapUserItemToUser(loginResponse.user),
            loginError = mapErrorsItemToLoginError(loginResponse.errors),
            message = loginResponse.message
        )

    fun mapRegisterResponseToRegister(registerResponse: RegisterResponse): Register =
        Register(
            token = registerResponse.token,
            user = mapUserItemToUser(registerResponse.user),
            registerError = mapErrorsItemToRegisterError(registerResponse.errors),
            message = registerResponse.message
        )

    private fun mapAddressComponentsItemToAddressComponents(addressComponentsItem: AddressComponentsItem): AddressComponents =
        AddressComponents(
            types = addressComponentsItem.types,
            shortName = addressComponentsItem.shortName,
            longName = addressComponentsItem.longName
        )

    fun mapMapsResponseToMaps(mapsResponse: MapsResponse): List<Maps>? =
        mapsResponse.results?.map { results ->
            Maps(
                formattedAddress = results.formattedAddress,
                addressComponents = results.addressComponents.map {
                    mapAddressComponentsItemToAddressComponents(it)
                }
            )
        }

    fun mapRatingResponseToRatings(ratingResponse: RatingResponse): List<Rating>? =
        ratingResponse.data?.map {
            Rating(
                transactionId = it.transactionId,
                image = it.image,
                product = mapProductItemToProduct(it.product),
                rating = it.rating,
                createdAt = it.createdAt,
                label = it.label,
                isRating = it.isRating,
                updatedAt = it.updatedAt,
                size = it.size,
                userId = it.userId,
                productId = it.productId,
                id = it.id,
                invoiceNumber = it.invoiceNumber
            )
        }

    fun mapCostItemToShippingCostItem(costItem: CostItem): ShippingCostItem =
        ShippingCostItem(
            note = costItem.note,
            etd = costItem.etd,
            value = costItem.value
        )

    fun mapCostsItemToShippingCost(costsItem: CostsItem): ShippingCost =
        ShippingCost(
            cost = costsItem.cost.map { mapCostItemToShippingCostItem(it) },
            service = costsItem.service,
            description = costsItem.description
        )

    fun mapShippingResponseToShipping(shippingResponse: ShippingResponse): List<Shipping>? =
        shippingResponse.rajaongkir?.results?.map { results ->
            Shipping(
                costs = results.costs.map { mapCostsItemToShippingCost(it) },
                code = results.code,
                name = results.name
            )
        }

    fun mapTransactionsResponseToTransactions(transactionsResponse: TransactionsResponse): List<Transaction>? =
        transactionsResponse.data?.map { results ->
            Transaction(
                productTransactions = results.productTransactions.map {
                    mapProductTransactionItemToTransactionProduct(it)
                },
                qtyTransaction = results.qtyTransaction,
                address = results.address,
                totalPrice = results.totalPrice,
                subtotalProducts = results.subtotalProducts,
                shippingCost = results.shippingCost,
                addressId = results.addressId,
                userId = results.userId,
                id = results.id,
                invoiceNumber = results.invoiceNumber,
                status = results.status
            )
        }
}