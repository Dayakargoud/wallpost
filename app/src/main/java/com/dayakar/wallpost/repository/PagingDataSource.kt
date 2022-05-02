package com.dayakar.wallpost.repository

import android.net.Uri
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dayakar.wallpost.model.Photo
import com.dayakar.wallpost.network.PexelApi

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

class PagingDataSource(private val pexelApi: PexelApi):PagingSource<String,Photo>() {

    override fun getRefreshKey(state: PagingState<String, Photo>): String? {
        return null
    }

    override val keyReuseSupported=true

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Photo> {
        val pageNumber=params.key?:1

        return try {

            val response=pexelApi.loadPhotos(page =pageNumber.toString())
            val pagedResponse=response.body()
            val photoList=pagedResponse?.photos?: emptyList()
            var nextPageNumber:String?=null

            Log.d("PagedData", "load: response= $pagedResponse")
            if (pagedResponse?.next_page!=null) {
                val uri = Uri.parse(pagedResponse.next_page)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageNumber = nextPageQuery
            }

            LoadResult.Page(
                data=photoList,
                prevKey = null,
                nextKey = nextPageNumber
            )
        }catch (e:Exception){
                LoadResult.Error(e)
        }
    }


}