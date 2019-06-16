package com.synchroniverse.bowwow

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.synchroniverse.bowwow.breedlist.ListViewModel
import com.synchroniverse.bowwow.errorhandling.BWError
import com.synchroniverse.bowwow.models.DogModel
import com.synchroniverse.bowwow.repository.DogRepository
import com.synchroniverse.bowwow.services.BWResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class ListViewModelTest {

    @get:Rule var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock lateinit var dogRepo : DogRepository

    private lateinit var listViewModel: ListViewModel

    @Mock lateinit var loadingIndicatorObserver: Observer<Boolean>

    @Mock lateinit var breedListObserver: Observer<List<DogModel>?>

    @Mock lateinit var errorObserver: Observer<BWError?>

    @Before
    fun setUp() {
        dogRepo = mock(DogRepository::class.java)
    }

    @Test
    fun loadingIndicatorIsTrue_whenViewModelInit() {
        listViewModel = ListViewModel(dogRepo)

        loadingIndicatorObserver = mock(Observer::class.java) as Observer<Boolean>
        listViewModel.loadingIndicator.observeForever(loadingIndicatorObserver)

        assertEquals(true, listViewModel.loadingIndicator.value)
    }

    @Test
    fun breedListUpdated_whenFetchAllBreedsCalledSuccessfully() {
        val liveData = MutableLiveData<BWResponse<List<DogModel>>>()
        val dog = DogModel("BreedName", "", false, mutableListOf())
        val response = BWResponse<List<DogModel>>(mutableListOf(dog), null)
        Mockito.`when`(dogRepo.fetchAllBreeds()).thenReturn(liveData).also { liveData.postValue(response) }

        listViewModel = ListViewModel(dogRepo)

        breedListObserver = mock(Observer::class.java) as Observer<List<DogModel>?>
        listViewModel.breedList.observeForever(breedListObserver)

        assertNull(listViewModel.error.value)
        assertNotNull(listViewModel.breedList.value)
        assertEquals(1, listViewModel.breedList.value?.size)
        assertEquals("BreedName", listViewModel.breedList.value?.get(0)?.breedName)
    }

    @Test
    fun errorUpdated_whenFetchAllBreedCalledUnSuccessfully() {
        val liveData = MutableLiveData<BWResponse<List<DogModel>>>()
        val errorResponse = BWResponse<List<DogModel>>(
            null,
            BWError("111", "Test Error Message", null)
        )
        Mockito.`when`(dogRepo.fetchAllBreeds()).thenReturn(liveData).also { liveData.postValue(errorResponse) }

        listViewModel = ListViewModel(dogRepo)

        errorObserver = mock(Observer::class.java) as Observer<BWError?>
        listViewModel.error.observeForever(errorObserver)

        assertNull(listViewModel.breedList.value)
        assertNotNull(listViewModel.error.value)
        assertEquals("111", listViewModel.error.value?.code)
    }

    @Test
    fun loadingIndicatorIsFalse_whenFetchAllBreedCalledUnSuccessfully() {
        errorUpdated_whenFetchAllBreedCalledUnSuccessfully()

        assertEquals(false, listViewModel.loadingIndicator.value)
    }

    @Test
    fun loadingIndicatorIsFalse_whenFetchAllBreedsCalledSuccessfully() {
        breedListUpdated_whenFetchAllBreedsCalledSuccessfully()

        assertEquals(false, listViewModel.loadingIndicator.value)
    }
}