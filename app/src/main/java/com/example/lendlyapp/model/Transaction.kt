package com.example.lendlyapp.model

import com.google.gson.annotations.SerializedName

data class Transaction(
    @SerializedName("id")              val id: String = "",
    @SerializedName("type")            val type: String = "",
    @SerializedName("title")           val title: String = "",
    @SerializedName("description")     val description: String = "",
    @SerializedName("amount")          val amount: Double = 0.0,
    @SerializedName("currency")        val currency: String = "PHP",
    @SerializedName("status")          val status: String = "",
    @SerializedName("date")            val date: String = "",
    @SerializedName("referenceNumber") val referenceNumber: String = "",
    @SerializedName("loanId")          val loanId: String? = null,
)

data class TransactionsApiResponse(
    @SerializedName("success")      val success: Boolean = false,
    @SerializedName("transactions") val transactions: List<Transaction> = emptyList(),
)
