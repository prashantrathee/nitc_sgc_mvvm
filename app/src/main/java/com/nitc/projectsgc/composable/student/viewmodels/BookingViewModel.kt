package com.nitc.projectsgc.composable.student.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import arrow.core.Either
import com.nitc.projectsgc.composable.student.repo.BookingRepo
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.models.Student
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val bookingRepo: BookingRepo
) : ViewModel() {

    private val _mentorTypes = MutableStateFlow<Either<String, List<String>>?>(null)
    val mentorTypes: StateFlow<Either<String, List<String>>?> = _mentorTypes.asStateFlow()

    fun getMentorTypes() {

        viewModelScope.launch {
            _mentorTypes.value = bookingRepo.getMentorTypes()
        }
    }

    private val _mentors = MutableStateFlow<Either<String, List<Mentor>>?>(null)
    val mentors: StateFlow<Either<String, List<Mentor>>?> = _mentors.asStateFlow()


    fun getMentors(mentorType: String) {
        viewModelScope.launch {
//            _mentors.value = null
            _mentors.value = bookingRepo.getMentorNames(mentorType)
        }
    }


    private val _availableTimeSlots = MutableStateFlow<Either<String, List<String>>?>(null)
    val availableTimeSlots: StateFlow<Either<String, List<String>>?> =
        _availableTimeSlots.asStateFlow()

    fun getAvailableTimeSlots(
        mentorType: String,
        mentorID: String,
        dateChosen: String
    ) {
        viewModelScope.launch {
            _availableTimeSlots.value = bookingRepo.getAvailableTimeSlots(
                mentorType,
                mentorID,
                dateChosen
            )
        }
    }


    suspend fun bookAppointment(appointment: Appointment): Boolean {
        val booked = withContext(Dispatchers.Main) {
            bookingRepo.bookAppointment(appointment)
        }
        return booked
    }


    suspend fun rescheduleAppointment(oldAppointment: Appointment,appointment: Appointment): Boolean {
        val rescheduled = withContext(Dispatchers.Main) {
            bookingRepo.rescheduleAppointment(oldAppointment,appointment)
        }
        return rescheduled
    }


}