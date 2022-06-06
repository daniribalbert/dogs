package com.daniribalbert.autodogs.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daniribalbert.autodogs.network.BaseResult
import com.daniribalbert.autodogs.network.dogapi.ILoadDogUseCase
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


class MainViewModel(private val dogsUseCase: ILoadDogUseCase) : ViewModel() {

    val dogImageLiveData = MutableLiveData<BaseResult<MutableList<String>>>()

    init {
        loadNewImage()
    }

    fun loadNewImage() {
        viewModelScope.launch {
            dogsUseCase.loadRandomDogImage().let {
                val data = dogImageLiveData.value?.result ?: mutableListOf()
                it.result?.let { dog -> data.add(dog.imageUrl) }
                dogImageLiveData.postValue(
                    BaseResult(
                        data,
                        it.error
                    )
                )
            }
        }
    }


}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
}
