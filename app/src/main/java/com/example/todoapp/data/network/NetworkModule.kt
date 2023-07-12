package com.example.todoapp.data.network

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer

/**
 * Module for creating OkHTTP Client.
 */
@Module
class NetworkModule {

    @Provides
    fun provideOkHTTPClient(): OkHttpClient {
        return OkHttpClient()
    }
}