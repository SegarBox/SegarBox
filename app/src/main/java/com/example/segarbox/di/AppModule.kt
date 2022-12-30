package com.example.segarbox.di

import com.example.core.domain.interactor.*
import com.example.core.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideAddressUseCase(interactor: Interactor): AddressUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(interactor: Interactor): CartUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideCheckoutUseCase(interactor: Interactor): CheckoutUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideDetailUseCase(interactor: Interactor): DetailUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideHomeUseCase(interactor: Interactor): HomeUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideInvoiceUseCase(interactor: Interactor): InvoiceUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(interactor: Interactor): LoginUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideMapsUseCase(interactor: Interactor): MapsUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideProfileUseCase(interactor: Interactor): ProfileUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRatingUseCase(interactor: Interactor): RatingUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideRegisterUseCase(interactor: Interactor): RegisterUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSearchUseCase(interactor: Interactor): SearchUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideShippingUseCase(interactor: Interactor): ShippingUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideTransactionUseCase(interactor: Interactor): TransactionUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSplashUseCase(interactor: Interactor): SplashUseCase

}