package com.nitc.projectsgc.composable.news

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
object NewsModule {

    @Provides
    @ViewModelScoped
    fun provideNewsRepo():NewsRepo{
        return NewsRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideNewsViewModel(newsRepo: NewsRepo):NewsViewModel{
        return NewsViewModel(newsRepo)
    }

}