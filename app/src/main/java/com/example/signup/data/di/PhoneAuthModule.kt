package com.example.signup.data.di

import com.example.signup.data.repository.PhoneAuthRepositoryImpl
import com.example.signup.domain.repository.PhoneAuthRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindPhoneAuthRepository(
        impl: PhoneAuthRepositoryImpl
    ): PhoneAuthRepository
}