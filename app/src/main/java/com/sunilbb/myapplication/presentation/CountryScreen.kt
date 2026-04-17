package com.sunilbb.myapplication.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import com.sunilbb.myapplication.GetCountriesQuery
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CountryScreen(viewModel: CountryViewModel, navController: NavController) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifeCycleOwner.lifecycle) {
        lifeCycleOwner.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.effect.collectLatest { event ->
                 when(event){
                     is CountryContract.UIEffect.navigateToDetail->{
                         navController.navigate("detail/${event.code}")
                     }
                     is CountryContract.UIEffect.ShowError -> {

                     }
                 }
            }
        }
    }

    CountryContent(
        state = state,
        onRetry = { viewModel.handleIntent(CountryContract.Intent.FetchCountries) },
        onCountryClick = { viewModel.handleIntent(CountryContract.Intent.NavigateToDetail(it)) }
    )
}

@Composable
fun CountryContent(
    state: CountryContract.UIState,
    onRetry: () -> Unit,
    onCountryClick: (String) -> Unit
) {
    when {
        state.isLoading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        state.error != null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.error ?: "Something went wrong")
                    Button(onClick = onRetry) {
                        Text("Retry")
                    }
                }
            }
        }
        else -> {
            CountryList(countries = state.countries, onClick = onCountryClick)
        }
    }
}

@Composable
fun CountryList(countries: List<GetCountriesQuery.Country>, onClick: (String) -> Unit) {
    LazyColumn {
        items(items = countries, key = { it.code }) { country ->
            Card(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "name = ${country.name}")
                    //Text(text = "emoji = ${country.emoji}")
                    Text(text = "code = ${country.code}")
                    Button(onClick = { onClick(country.code) }) {
                        Text("Click me")
                    }
                }
            }
        }
    }
}
