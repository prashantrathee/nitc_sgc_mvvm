package com.nitc.projectsgc.composable.util.storage

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object StorageModule {

    @Provides
    @ViewModelScoped
    fun provideStorageManager(context: Context): StorageManager {
        return StorageManagerImpl(context)
    }


}