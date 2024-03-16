package com.nitc.projectsgc.composable.login

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object LoginModule {

    @Provides
    @ViewModelScoped
    fun provideLoginRepo():LoginRepo{
        return LoginRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideLoginViewModel(loginRepo: LoginRepo):LoginViewModel{
        return LoginViewModel(loginRepo)
    }

}