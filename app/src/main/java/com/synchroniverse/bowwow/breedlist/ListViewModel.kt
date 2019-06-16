package com.synchroniverse.bowwow.breedlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.synchroniverse.bowwow.repository.DogRepository
import com.synchroniverse.bowwow.errorhandling.BWError
import com.synchroniverse.bowwow.models.DogModel
import com.synchroniverse.bowwow.services.BWResponse

class ListViewModel(repository: DogRepository) : ViewModel() {

    private val apiResponse : LiveData<BWResponse<List<DogModel>>>

    val loadingIndicator = MutableLiveData<Boolean>()
    val error : LiveData<BWError?>
    val breedList : LiveData<List<DogModel>?>

    private fun handleError(response: BWResponse<List<DogModel>>) : BWError?{
        if (!response.isSuccess()) {
            loadingIndicator.postValue(false)
            return response.error
        }
        return null
    }

    private fun handleSuccess(response: BWResponse<List<DogModel>>) : List<DogModel>?{
        if (response.isSuccess()) {
            loadingIndicator.postValue(false)
            return response.response
        }
        return null
    }

    init {
        loadingIndicator.postValue(true)
        apiResponse = repository.fetchAllBreeds()
        error = Transformations.map(apiResponse, ::handleError)
        breedList = Transformations.map(apiResponse, ::handleSuccess)
    }
}