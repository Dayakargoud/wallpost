package com.dayakar.wallpost.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.dayakar.wallpost.repository.PhotosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * @Created By DAYAKAR GOUD BANDARI on 30-04-2022.
 */

@HiltViewModel
class PhotosViewModel @Inject constructor(repository: PhotosRepository):ViewModel() {

    val photos=repository.photosList().cachedIn(viewModelScope)

}