package com.example.kotlincurrencyconverter.di

import com.example.kotlincurrencyconverter.data.CurrencyApi
import com.example.kotlincurrencyconverter.main.DefaultMainRepository
import com.example.kotlincurrencyconverter.main.MainRepository
import com.example.kotlincurrencyconverter.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://v6.exchangerate-api.com/v6/"

@Module
//these dependencies live as long as our applications
@InstallIn(SingletonComponent::class)
object AppModule {

    //we will put the functions that will create the dependencies we want to inject
    //and app module will contain singletons of our app, the dependencies that live as long as
    //our application does

    @Singleton
    @Provides
    //we provide this instance so it can inject
    fun provideCurrencyApi(): CurrencyApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
            //converting json response to data classes
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyApi::class.java)

    @Singleton
    @Provides
    //singleton provides function that provides our main repository
    fun provideMainRepository(api: CurrencyApi): MainRepository = DefaultMainRepository(api)

    @Singleton
    @Provides
    //singleton provides function that provides our dispatchers
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined
    }
}