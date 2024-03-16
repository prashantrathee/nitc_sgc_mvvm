package com.nitc.projectsgc.composable.admin

import com.nitc.projectsgc.composable.admin.repo.MentorsRepo
import com.nitc.projectsgc.composable.admin.repo.StudentsRepo
import com.nitc.projectsgc.composable.admin.viewmodels.AdminViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object AdminModule {
    @Provides
    @ViewModelScoped
    fun provideStudentsRepo(): StudentsRepo {
        return StudentsRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideMentorsRepo(): MentorsRepo {
        return MentorsRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideAdminViewModel(studentsRepo: StudentsRepo,mentorsRepo: MentorsRepo): AdminViewModel {
        return AdminViewModel(studentsRepo,mentorsRepo)
    }

//
//    @Provides
//    @ViewModelScoped
//    fun provideStudentListViewModel(studentsRepo: StudentsRepo): StudentListViewModel {
//        return StudentListViewModel(studentsRepo)
//    }
//
//
//    @Provides
//    @ViewModelScoped
//    fun provideMentorListViewModel(mentorsRepo: MentorsRepo): MentorListViewModel {
//        return MentorListViewModel(mentorsRepo)
//    }



}