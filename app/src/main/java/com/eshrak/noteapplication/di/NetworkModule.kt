package com.eshrak.noteapplication.di

import com.eshrak.noteapplication.data.api.AuthInterceptor
import com.eshrak.noteapplication.data.api.NoteApi
import com.eshrak.noteapplication.data.api.QuoteAPI
import com.eshrak.noteapplication.data.api.UserAPI
import com.eshrak.noteapplication.util.Constants.BASE_URL
import com.eshrak.noteapplication.util.Constants.BASE_URL_2
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
    @RetrofitBuilder1
    fun provideRetrofitBuilder1(): Retrofit.Builder {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
    }


    @Singleton
    @Provides
    @RetrofitBuilder2
    fun provideRetrofitBuilder2(): Retrofit.Builder {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL_2)
    }


    @Singleton
    @Provides
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }


    @Singleton
    @Provides
    fun providesUserAPI(@RetrofitBuilder1 retrofitBuilder: Retrofit.Builder): UserAPI {
        return retrofitBuilder.build().create(UserAPI::class.java)
    }

    @Singleton
    @Provides
    fun providesNoteAPI(
        @RetrofitBuilder1 retrofitBuilder: Builder, okHttpClient: OkHttpClient
    ): NoteApi {
        return retrofitBuilder.client(okHttpClient).build().create(NoteApi::class.java)
    }


    @Singleton
    @Provides
    fun providesQuoteAPI(@RetrofitBuilder2 retrofitBuilder: Retrofit.Builder): QuoteAPI {
        return retrofitBuilder.build().create(QuoteAPI::class.java)
    }

}