package com.synchroniverse.bowwow.breeddetail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.synchroniverse.bowwow.errorhandling.BWError
import com.synchroniverse.bowwow.models.ImageApiModel
import com.synchroniverse.bowwow.repository.DogRepository
import com.synchroniverse.bowwow.services.BWResponse
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class DetailViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    lateinit var dogRepo : DogRepository

    private lateinit var detailViewModel: DetailViewModel

    @Mock lateinit var loadingIndicatorObserver: Observer<Boolean>

    @Mock lateinit var imageListObserver: Observer<List<String>?>

    @Mock lateinit var errorObserver: Observer<BWError?>

    @Before
    fun setUp() {
        dogRepo = Mockito.mock(DogRepository::class.java)
    }

    @Test
    fun imageListUpdated_whenImagesFetchedOfOnlyBreed(){
        val liveData = MutableLiveData<BWResponse<ImageApiModel>>()
        val images = ImageApiModel("success", mutableListOf("URL 1", "URL 2", "URL 3"))
        val response = BWResponse(images, null)
        Mockito.`when`(dogRepo.fetchImages(eq("BreedName"), eq(""))).thenReturn(liveData).also { liveData.postValue(response) }

        detailViewModel = DetailViewModel(dogRepo)
        detailViewModel.fetchImages("BreedName", "")
        imageListObserver = mock(Observer::class.java) as Observer<List<String>?>
        detailViewModel.imageList.observeForever(imageListObserver)

        assertNull(detailViewModel.error.value)
        assertNotNull(detailViewModel.imageList.value)
        assertEquals(3, detailViewModel.imageList.value?.size)
        assertEquals("URL 2", detailViewModel.imageList.value?.get(1))
    }

    @Test
    fun imageListUpdated_whenImagesFetchedOfBreedAndSubBreed() {
        val liveData = MutableLiveData<BWResponse<ImageApiModel>>()
        val images = ImageApiModel("success", mutableListOf("URL 1", "URL 2", "URL 3"))
        val response = BWResponse(images, null)
        Mockito.`when`(dogRepo.fetchImages(eq("BreedName"), eq("SubBreedName"))).thenReturn(liveData).also { liveData.postValue(response) }

        detailViewModel = DetailViewModel(dogRepo)
        detailViewModel.fetchImages("BreedName", "SubBreedName")
        imageListObserver = mock(Observer::class.java) as Observer<List<String>?>
        detailViewModel.imageList.observeForever(imageListObserver)

        assertNull(detailViewModel.error.value)
        assertNotNull(detailViewModel.imageList.value)
        assertEquals(3, detailViewModel.imageList.value?.size)
        assertEquals("URL 2", detailViewModel.imageList.value?.get(1))
    }

    @Test
    fun errorShown_whenFetchImagesForBreedAndSubBreedFails() {
        val liveData = MutableLiveData<BWResponse<ImageApiModel>>()
        val errorResponse = BWResponse<ImageApiModel>(
            null,
            BWError("111", "Test Error Message", null)
        )
        Mockito.`when`(dogRepo.fetchImages(eq("BreedName"), eq("SubBreedName"))).thenReturn(liveData).also { liveData.postValue(errorResponse) }
        detailViewModel = DetailViewModel(dogRepo)
        detailViewModel.fetchImages("BreedName", "SubBreedName")
        errorObserver = mock(Observer::class.java) as Observer<BWError?>
        detailViewModel.error.observeForever(errorObserver)

        assertNotNull(detailViewModel.error.value)
        assertNull(detailViewModel.imageList.value)
        assertEquals("111", detailViewModel.error.value?.code)
    }

    @Test
    fun loadingIndicatorIsTrue_whenFetchImagesIsCalled() {
        val liveData = MutableLiveData<BWResponse<ImageApiModel>>()
        val images = ImageApiModel("success", mutableListOf("URL 1", "URL 2", "URL 3"))
        val response = BWResponse(images, null)
        Mockito.`when`(dogRepo.fetchImages(eq("BreedName"), eq("SubBreedName"))).thenReturn(liveData).also { liveData.postValue(response) }

        detailViewModel = DetailViewModel(dogRepo)
        detailViewModel.fetchImages("BreedName", "SubBreedName")
        loadingIndicatorObserver = mock(Observer::class.java) as Observer<Boolean>
        detailViewModel.loadingIndicator.observeForever(loadingIndicatorObserver)

        assertEquals(true, detailViewModel.loadingIndicator.value)
    }

    @Test
    fun loadingIndicatorIsFalse_whenFetchImagesCalledUnSuccessfully() {
        errorShown_whenFetchImagesForBreedAndSubBreedFails()

        assertEquals(false, detailViewModel.loadingIndicator.value)
    }

    @Test
    fun loadingIndicatorIsFalse_whenFetchImagesCalledSuccessfully() {
        imageListUpdated_whenImagesFetchedOfBreedAndSubBreed()

        assertEquals(false, detailViewModel.loadingIndicator.value)
    }
}