package com.synchroniverse.bowwow.breeddetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.synchroniverse.bowwow.errorhandling.BWError
import com.synchroniverse.bowwow.models.ImageApiModel
import com.synchroniverse.bowwow.repository.DogRepository
import com.synchroniverse.bowwow.services.BWResponse

class DetailViewModel(private val repository: DogRepository) : ViewModel() {

    private val apiResponse : LiveData<BWResponse<ImageApiModel>>

    private val mBreed = MutableLiveData<Pair<String, String?>>()

    val loadingIndicator = MutableLiveData<Boolean>()
    var error : LiveData<BWError?>
    var imageList : LiveData<List<String>>

    init {
        apiResponse = Transformations.switchMap(mBreed) {
            breed -> repository.fetchImages(breed.first, breed.second)
        }
        error = Transformations.map(apiResponse, ::handleError)
        imageList = Transformations.map(apiResponse, ::handleSuccess)
    }

    private fun handleError(response: BWResponse<ImageApiModel>) : BWError?{
        if (!response.isSuccess()) {
            loadingIndicator.postValue(false)
            return response.error
        }
        return null
    }

    private fun handleSuccess(response: BWResponse<ImageApiModel>) : List<String>?{
        if (response.isSuccess()) {
            loadingIndicator.postValue(false)
            return response.response?.message
        }
        return null
    }

    fun fetchImages(breed: String, subBreed: String?) {
        loadingIndicator.postValue(true)
        mBreed.value = Pair(breed, subBreed)
    }
}