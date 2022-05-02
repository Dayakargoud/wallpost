package com.dayakar.wallpost.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.dayakar.wallpost.model.Photo
import com.dayakar.wallpost.network.PexelApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

class PhotosRepository @Inject constructor(val pexelApi:PexelApi) {


    fun photosList():Flow<PagingData<Photo>> =
        Pager(
            config = PagingConfig(pageSize = 5, enablePlaceholders = false),
            pagingSourceFactory = {PagingDataSource(pexelApi)}
        ).flow


}