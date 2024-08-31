package com.example.jirodivisas.currency

import com.google.gson.annotations.SerializedName

data class ExchangeRates(
    val result: String,
    val documentation: String,

    @SerializedName("terms_of_use")
    val termsOfUse: String,

    @SerializedName("time_last_update_unix")
    val timeLastUpdateUnix: Long,

    @SerializedName("time_last_update_utc")
    val timeLastUpdateUTC: String,

    @SerializedName("time_next_update_unix")
    val timeNextUpdateUnix: Long,

    @SerializedName("time_next_update_utc")
    val timeNextUpdateUTC: String,

    @SerializedName("base_code")
    val baseCode: String,

    @SerializedName("conversion_rates")
    val conversionRates: Map<String, Double>
)