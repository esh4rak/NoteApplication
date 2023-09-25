package com.eshrak.noteapplication.di

import com.eshrak.noteapplication.data.api.AuthInterceptor
import com.eshrak.noteapplication.data.api.NoteApi
import com.eshrak.noteapplication.data.api.UserAPI
import com.eshrak.noteapplication.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {


    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }


    @Singleton
    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }


    @Singleton
    @Provides
    fun providesUserAPI(retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteAPI(retrofitBuilder: Builder, okHttpClient: OkHttpClient): NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }

}