package com.daniribalbert.autodogs

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.daniribalbert.autodogs.network.DogsRepository
import com.daniribalbert.autodogs.network.model.ApiError
import com.daniribalbert.autodogs.network.model.DogApiResponse
import com.daniribalbert.autodogs.rules.CoroutineRule
import com.daniribalbert.autodogs.ui.main.MainViewModel
import com.daniribalbert.autodogs.network.BaseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineScope
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

/**
 * Test MainViewModel.
 */
@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class TestMainViewModel {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Mock
    lateinit var mockRepository: DogsRepository

    @Mock
    lateinit var mockApiResult: BaseResult<DogApiResponse>

    @Mock
    lateinit var mockDogApiResponse: DogApiResponse

    @Mock
    lateinit var mockError: ApiError

    @Mock
    lateinit var mockObserver: Observer<BaseResult<*>>

    private lateinit var viewModel: MainViewModel


    @Before
    fun setupTests() {
        Mockito.`when`(mockApiResult.result).thenReturn(mockDogApiResponse)
        Mockito.`when`(mockDogApiResponse.message).thenReturn(TEST_IMAGE_URL)

        viewModel = MainViewModel(mockRepository)
    }

    @Test
    fun testLoadNewImage_Success() {
        TestCoroutineScope().launch {
            val result = BaseResult(
                mockDogApiResponse,
                null
            )
            Mockito.`when`(mockRepository.loadDogImage()).thenReturn(mockApiResult)

            viewModel.dogImageLiveData.observeForever(mockObserver)
            viewModel.loadNewImage()

            Mockito.verify(mockObserver).onChanged(result)
            Assert.assertEquals(result.result, viewModel.dogImageLiveData.value?.result)
            Assert.assertNull(viewModel.dogImageLiveData.value?.error)
        }
    }

    @Test
    fun testLoadNewImage_Error() {
        TestCoroutineScope().launch {
            val result =
                BaseResult(null, mockError)
            Mockito.`when`(mockRepository.loadDogImage()).thenReturn(result)

            viewModel.dogImageLiveData.observeForever(mockObserver)
            viewModel.loadNewImage()

            Mockito.verify(mockObserver).onChanged(result)
            Assert.assertEquals(result.result, viewModel.dogImageLiveData.value?.error)
            Assert.assertNull(viewModel.dogImageLiveData.value?.result)
        }
    }

    companion object {
        private const val TEST_IMAGE_URL = "something.jpg"
    }
}
