package com.synchroniverse.bowwow.services

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ApiClient {

    companion object RetrofitFactory {
        private val BaseUrl = "https://dog.ceo/api/"

        fun getClient() : Retrofit = Retrofit.Builder().baseUrl(BaseUrl)
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    }
}