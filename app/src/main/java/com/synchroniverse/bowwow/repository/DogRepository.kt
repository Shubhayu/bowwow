package com.synchroniverse.bowwow.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.synchroniverse.bowwow.errorhandling.BWError
import com.synchroniverse.bowwow.models.DogModel
import com.synchroniverse.bowwow.models.ImageApiModel
import com.synchroniverse.bowwow.services.ApiClient
import com.synchroniverse.bowwow.services.BWResponse
import com.synchroniverse.bowwow.services.DogApiService
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogRepository {

    fun fetchAllBreeds() : LiveData<BWResponse<List<DogModel>>> {
        val returnValue = MutableLiveData<BWResponse<List<DogModel>>>()
        val response = ApiClient.getClient().create(DogApiService::class.java).getBreedListAsync()
        response.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorResponse = BWResponse<List<DogModel>>(
                    error = BWError(
                        "1001",
                        t.message!!,
                        t
                    )
                )
                returnValue.postValue(errorResponse)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val jsonObj = JSONObject(response.body()?.string())
                    var dogList = mutableListOf<DogModel>()
                    if (jsonObj.getString("status") == "success") {
                        val message = jsonObj.getJSONObject("message")
                        for( key in message.keys()) {
                            val newDog = DogModel(key)
                            if (message.getJSONArray(key).length() > 0) {
                                for(i in 0 until message.getJSONArray(key).length()-1) {
                                    val subBreedDog = DogModel(key, message.getJSONArray(key).get(i).toString(), true)
                                    newDog.subBreedList.add(subBreedDog)
                                }
                            }
                            dogList.add(newDog)
                        }
                        val requestResponse = BWResponse<List<DogModel>>(dogList)
                        returnValue.postValue(requestResponse)
                    } else if (jsonObj.getString("status") == "error") {
                        val errorMessage = jsonObj.getString("message")
                        val errorCode = jsonObj.getString("code")
                        val requestResponse = BWResponse<List<DogModel>>(
                            error = BWError(
                                errorCode,
                                errorMessage
                            )
                        )
                        returnValue.postValue(requestResponse)
                    }
                }
            }
        })

        return returnValue
    }

    fun fetchImages(breed: String, subBreed: String?) : LiveData<BWResponse<ImageApiModel>> {
        val returnValue = MutableLiveData<BWResponse<ImageApiModel>>()
        val apiService = ApiClient.getClient().create(DogApiService::class.java)
        val response: Call<ImageApiModel>
        response = if (!subBreed.isNullOrEmpty()) {
            apiService.getSubBreedImagesAsync(breed, subBreed)
        } else {
            apiService.getBreedImagesAsync(breed)
        }

        response.enqueue(object : Callback<ImageApiModel> {
            override fun onFailure(call: Call<ImageApiModel>, t: Throwable) {
                val errorResponse = BWResponse<ImageApiModel>(
                    error = BWError(
                        "1002",
                        t.message!!,
                        t
                    )
                )
                returnValue.postValue(errorResponse)
            }

            override fun onResponse(call: Call<ImageApiModel>, response: Response<ImageApiModel>) {
                if (response.isSuccessful) {
                    val requestResponse = BWResponse(response.body())
                    returnValue.postValue(requestResponse)
                } else {
                    val errorObj = JSONObject(response.errorBody()?.string())
                    if (errorObj.getString("status") == "error") {
                        val errorMessage = errorObj.getString("message")
                        val errorCode = errorObj.getString("code")
                        val requestResponse = BWResponse<ImageApiModel>(
                            error = BWError(
                                errorCode,
                                errorMessage
                            )
                        )
                        returnValue.postValue(requestResponse)
                    }
                }
            }
        })
        return returnValue
    }
}