package com.nitc.projectsgc.composable.student

import com.nitc.projectsgc.MyApplication
import com.nitc.projectsgc.composable.student.repo.StudentRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object StudentModule {

    @Provides
    @ViewModelScoped
    fun provideStudentRepo():StudentRepo{
        return StudentRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideStudentViewModel(studentRepo: StudentRepo):StudentViewModel{
        return StudentViewModel(studentRepo)
    }

}