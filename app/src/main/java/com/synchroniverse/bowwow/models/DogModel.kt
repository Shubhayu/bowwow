package com.synchroniverse.bowwow.models

data class DogModel(
    val breedName : String,
    val subBreedName: String = "",
    val isSubBreed: Boolean = false,
    val subBreedList : MutableList<DogModel> = mutableListOf()
)