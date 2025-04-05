package com.example.smartinventory.data

import com.example.smartinventory.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WebService {

    @GET("{product}")
    suspend fun getProducts(@Path("product") product: String): Response<List<Product>>
}