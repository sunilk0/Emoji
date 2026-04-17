package com.sunilbb.myapplication.presentation

import com.sunilbb.myapplication.GetCountriesQuery

class CountryContract {
    data class UIState(
        val isLoading: Boolean = false,
        val countries: List<GetCountriesQuery.Country> = emptyList(),
        val error: String? = null
    )


    sealed class Intent {
        object FetchCountries : Intent()
        data class NavigateToDetail(val code: String) :Intent()
    }

    sealed class UIEffect {
        data class ShowError(val message: String) : UIEffect()

        data class navigateToDetail(val code: String) : UIEffect()

    }
}

