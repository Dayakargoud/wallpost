package com.dayakar.wallpost.network

import com.dayakar.wallpost.model.PexelAPIResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

interface PexelApi {

    companion object{
      //  const val BASE_URL="https://api.pexels.com/v1/curated?page=2&per_page=10"
        const val BASE_URL="https://api.pexels.com"
    }

    @GET("v1/curated")
    suspend fun loadPhotos(@Query("page") page:String="1",@Query("per_page") perPage:String="10"):Response<PexelAPIResponse>

}