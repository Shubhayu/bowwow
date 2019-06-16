package com.synchroniverse.bowwow.breeddetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.synchroniverse.bowwow.repository.DogRepository

class DetailViewModelFactory(private val repository: DogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DetailViewModel(repository = repository) as T
    }
}