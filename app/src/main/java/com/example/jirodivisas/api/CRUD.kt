package com.example.jirodivisas.api

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import com.example.jirodivisas.currency.ExchangeRates
import com.google.gson.GsonBuilder
import com.google.gson.InstanceCreator
import com.google.gson.JsonDeserializationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import io.github.cdimascio.dotenv.dotenv
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

class CRUD() : CoroutineScope {
    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    val dotenv = dotenv {
        directory = "/assets"
        filename = "env"
    }

    private val URL_API = dotenv["URL_API"]

    private fun getClient(): OkHttpClient {
        var login = HttpLoggingInterceptor()
        login.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder().addInterceptor(login).build()
    }

    private fun getRetrofit(): Retrofit {
        val gson = GsonBuilder().setLenient().create()

        return Retrofit.Builder().baseUrl(URL_API).client(getClient())
            .addConverterFactory(GsonConverterFactory.create(gson)).build()
    }

    suspend fun getLatestRates(currency: String): ExchangeRates? {
        try {
            val response = getRetrofit().create(ApiService::class.java).getCurrency(currency)
            val result = response.body()
            return if (response.isSuccessful && result != null) {
                result
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e("Error", e.message.toString())
        }
        return null
    }
}