package com.juliengabryelewicz.sutom.di

import android.content.Context
import com.juliengabryelewicz.sutom.domain.repository.SutomRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module()
class AppModule {

    @Provides
    @Singleton
    fun provideSutomRepository(
        @ApplicationContext context: Context,
    ): SutomRepository =
        SutomRepository(context)
}