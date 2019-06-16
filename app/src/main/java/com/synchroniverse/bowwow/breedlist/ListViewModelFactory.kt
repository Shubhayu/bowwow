package com.synchroniverse.bowwow.breedlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.synchroniverse.bowwow.repository.DogRepository

class ListViewModelFactory(private val repository: DogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ListViewModel(repository) as T
    }
}