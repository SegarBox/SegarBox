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

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideAddressUseCase(interactor: Interactor): AddressUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideCartUseCase(interactor: Interactor): CartUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideCheckoutUseCase(interactor: Interactor): CheckoutUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideDetailUseCase(interactor: Interactor): DetailUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideHomeUseCase(interactor: Interactor): HomeUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideInvoiceUseCase(interactor: Interactor): InvoiceUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideLoginUseCase(interactor: Interactor): LoginUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideMapsUseCase(interactor: Interactor): MapsUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideProfileUseCase(interactor: Interactor): ProfileUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideRatingUseCase(interactor: Interactor): RatingUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideRegisterUseCase(interactor: Interactor): RegisterUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideSearchUseCase(interactor: Interactor): SearchUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideShippingUseCase(interactor: Interactor): ShippingUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideTransactionUseCase(interactor: Interactor): TransactionUseCase

    @Singleton
    @Binds
    @ViewModelScoped
    abstract fun provideSplashUseCase(interactor: Interactor): SplashUseCase

}