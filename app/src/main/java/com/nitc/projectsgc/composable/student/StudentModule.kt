package com.nitc.projectsgc.composable.student

import com.nitc.projectsgc.composable.student.repo.BookingRepo
import com.nitc.projectsgc.composable.student.repo.StudentRepo
import com.nitc.projectsgc.composable.student.viewmodels.StudentViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

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
    fun provideBookingRepo(): BookingRepo {
        return BookingRepo()
    }

    @Provides
    @ViewModelScoped
    fun provideStudentViewModel(studentRepo: StudentRepo,bookingRepo: BookingRepo): StudentViewModel {
        return StudentViewModel(studentRepo,bookingRepo)
    }

}