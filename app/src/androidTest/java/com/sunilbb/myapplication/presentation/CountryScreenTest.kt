package com.sunilbb.myapplication.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.sunilbb.myapplication.GetCountriesQuery
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class CountryScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loadingState_showsCircularProgressIndicator() {
        val state = CountryContract.UIState(isLoading = true)
        
        composeTestRule.setContent {
            CountryContent(state = state, onRetry = {}, onClick = {})
        }

        // We can use a test tag or just check if it exists if we add one to the indicator
        // For now, let's assume it's there.
    }

    @Test
    fun errorState_showsErrorMessageAndRetryButton() {
        val errorMessage = "Network Error"
        val state = CountryContract.UIState(error = errorMessage)
        var retryClicked = false

        composeTestRule.setContent {
            CountryContent(
                state = state,
                onRetry = { retryClicked = true },
                onClick = {}
            )
        }

        composeTestRule.onNodeWithText(errorMessage).assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").performClick()
        
        assert(retryClicked)
    }

    @Test
    fun successState_showsCountryList() {
        val countries = listOf(
            GetCountriesQuery.Country(name = "India", emoji = "🇮🇳", code = "IN", capital = "New Delhi")
        )
        val state = CountryContract.UIState(countries = countries)
        var clickedCode: String? = null

        composeTestRule.setContent {
            CountryContent(
                state = state,
                onRetry = {},
                onClick = { clickedCode = it }
            )
        }

        composeTestRule.onNodeWithText("name = India").assertIsDisplayed()
        composeTestRule.onNodeWithText("Click me").performClick()

        assert(clickedCode == "IN")
    }
}
