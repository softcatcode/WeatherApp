package com.softcat.data.network.api

import com.softcat.data.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.Locale

object ApiFactory {

    private const val BASE_URL = "https://api.weatherapi.com/v1/"
    private const val KEY_PARAM = "key"
    private const val LANGUAGE_PARAM = "lang"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalReq = chain.request()
            val newUrl = originalReq.url.newBuilder()
                .addQueryParameter(KEY_PARAM, BuildConfig.WEATHER_API_KEY)
                .addQueryParameter(LANGUAGE_PARAM, Locale.getDefault().language)
                .build()
            val newReq = originalReq.newBuilder().url(newUrl).build()
            chain.proceed(newReq)
        }.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        ).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}