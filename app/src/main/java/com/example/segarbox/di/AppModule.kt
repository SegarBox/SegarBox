package com.example.segarbox.di

import com.example.core.domain.interactor.*
import com.example.core.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideAddressUseCase(addressInteractor: AddressInteractor): AddressUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(cartInteractor: CartInteractor): CartUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCheckoutUseCase(checkoutInteractor: CheckoutInteractor): CheckoutUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDetailUseCase(detailInteractor: DetailInteractor): DetailUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHomeUseCase(homeInteractor: HomeInteractor): HomeUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideInvoiceUseCase(invoiceInteractor: InvoiceInteractor): InvoiceUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(loginInteractor: LoginInteractor): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideMapsUseCase(mapsInteractor: MapsInteractor): MapsUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideProfileUseCase(profileInteractor: ProfileInteractor): ProfileUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRatingUseCase(ratingInteractor: RatingInteractor): RatingUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRegisterUseCase(registerInteractor: RegisterInteractor): RegisterUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSearchUseCase(searchInteractor: SearchInteractor): SearchUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideShippingUseCase(shippingInteractor: ShippingInteractor): ShippingUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideTransactionUseCase(transactionInteractor: TransactionInteractor): TransactionUseCase

}