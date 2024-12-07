package com.example.harjoitusprojekti

import com.example.harjoitusprojekti.models.Product
import retrofit2.http.GET

interface ApiService {
    @GET("products")
    suspend fun getProducts(): List<Product>
}


