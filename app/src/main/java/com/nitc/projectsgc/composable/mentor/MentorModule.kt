package com.nitc.projectsgc.composable.mentor

import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
import com.nitc.projectsgc.composable.mentor.repo.MentorRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped


@Module
@InstallIn(ViewModelComponent::class)
object MentorModule {


    @Provides
    @ViewModelScoped
    fun provideStudentsRepo(): StudentsRepo {
        return StudentsRepo()
    }
    @Provides
    @ViewModelScoped
    fun provideMentorRepo(): MentorRepo {
        return MentorRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideMentorViewModel(mentorRepo: MentorRepo,studentsRepo: StudentsRepo): MentorViewModel {
        return MentorViewModel(mentorRepo,studentsRepo)
    }
}