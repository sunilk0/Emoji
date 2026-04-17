package com.sunilbb.myapplication.data.repository

import com.apollographql.apollo.ApolloClient
import com.sunilbb.myapplication.GetCountriesQuery
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CountryRepository(private val apolloClient: ApolloClient){
    suspend fun getCountries():List<GetCountriesQuery.Country> {
        val response = apolloClient.query(GetCountriesQuery()).execute()
        return response.data?.countries ?: emptyList()
    }



    }

