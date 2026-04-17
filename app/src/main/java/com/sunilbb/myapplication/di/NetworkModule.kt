package com.sunilbb.myapplication.di

import com.apollographql.apollo.ApolloClient
import com.sunilbb.myapplication.data.repository.CountryRepository

val apolloClient = ApolloClient.
Builder().
serverUrl("https://countries.trevorblades.com/graphql")
    .build()

val repository = CountryRepository(apolloClient = apolloClient)