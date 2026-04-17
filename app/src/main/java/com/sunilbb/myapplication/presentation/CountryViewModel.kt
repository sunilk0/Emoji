package com.sunilbb.myapplication.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sunilbb.myapplication.data.repository.CountryRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class CountryViewModel(private val countryRepository: CountryRepository) : ViewModel() {

    private val _state = MutableStateFlow(CountryContract.UIState())
    val state: StateFlow<CountryContract.UIState> = _state.asStateFlow()

    //shared flow
    private val _effect = MutableSharedFlow<CountryContract.UIEffect>()
    val effect: SharedFlow<CountryContract.UIEffect> = _effect.asSharedFlow()


    init {
        fetchCountries()
    }

    fun handleIntent(intent: CountryContract.Intent) {
        when (intent) {
            is CountryContract.Intent.FetchCountries ->{ fetchCountries() }
            is CountryContract.Intent.NavigateToDetail -> {
                viewModelScope.launch {
                    _effect.emit(CountryContract.UIEffect.navigateToDetail(intent.code))
                }
            }
        }
    }


   private  fun fetchCountries() {
      viewModelScope.launch {
          _state.value = _state.value.copy(isLoading = true, error = null)

          try{
              val countries = countryRepository.getCountries()
              _state.value = _state.value.copy(isLoading = false, countries = countries)

          }catch (ex: Exception){
              _state.value = _state.value.copy(isLoading = false, error = ex.message)
          }

      }
    }
}

