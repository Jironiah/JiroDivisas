package com.example.jirodivisas.api

import com.example.jirodivisas.currency.ExchangeRates
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("{currency}")
    suspend fun getCurrency(@Path("currency") currency: String): Response<ExchangeRates>
}