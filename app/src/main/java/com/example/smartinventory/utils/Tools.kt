package com.example.smartinventory.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import com.example.smartinventory.R
import com.google.gson.Gson
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class Tools {
    companion object {

        fun getSalutationBasedOnTime(): String {
            val date = Date()
            val cal = Calendar.getInstance()
            cal.time = date
            val hour = cal[Calendar.HOUR_OF_DAY]
            var greeting: String? = null
            return when (hour) {
                in 12..16 -> {
                    "Good Afternoon".also { greeting = it }
                }

                in 17..20 -> {
                    "Good Evening".also { greeting = it }
                }

                in 21..23 -> {
                    "Good Night".also { greeting = it }
                }

                else -> {
                    "Good Morning".also { greeting = it }
                }
            }
        }

        fun handleErrorResponse(errorBody: String?): ApiResponse.ErrorResponse? =
            Gson().fromJson(errorBody, ApiResponse.ErrorResponse::class.java)

        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }

        fun showToast(context: Context?, message: String?) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }

        fun formatPrice(context: Context?, amount: String): String? {
            try {
                return context?.let { formatToNaira(it, BigDecimal(amount)) }
            } catch (nfe: NumberFormatException) {
                nfe.printStackTrace()
                return amount
            }
        }

        private fun formatToNaira(context: Context, amount: BigDecimal): String {
            return String.format(
                Locale.US, context.resources.getString(R.string.money_naira_double_format),
                amount.toDouble()
            )
        }

        fun reformatDate(dateString: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(dateString)
                val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                outputFormat.format(date!!)
            } catch (e: Exception) {
                dateString
            }
        }

    }
}