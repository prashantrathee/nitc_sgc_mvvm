package com.nitc.projectsgc.composable

import android.app.Application
import android.content.Context
import com.nitc.projectsgc.MyApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {


    @Provides
    @Singleton
    fun provideContext(application:Application):Context{
        return application.applicationContext
    }
}