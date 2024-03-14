package com.nitc.projectsgc.student.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import com.nitc.projectsgc.models.Appointment
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.admin.access.MentorsAccess
import com.nitc.projectsgc.student.access.BookingAccess
import com.nitc.projectsgc.databinding.FragmentBookingBinding
import java.util.*
import com.nitc.projectsgc.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class BookingFragment : Fragment() {
    lateinit var binding : FragmentBookingBinding
    lateinit var mentorType : String
    var mentorNameSelected = "NA"
    var mentorID = "NA"
    private val sharedViewModel:SharedViewModel by activityViewModels()
    var selectedDate = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentBookingBinding.inflate(inflater,container,false)
        val database : FirebaseDatabase = FirebaseDatabase.getInstance()
        var reference : DatabaseReference = database.reference.child(sharedViewModel.currentInstitution.username!!).child("types")
        var mentorTypeSelected = "NA"

        if(sharedViewModel.rescheduling){
            mentorTypeSelected = sharedViewModel.reschedulingAppointment.mentorType.toString()
            mentorNameSelected = sharedViewModel.reschedulingMentorName
            binding.mentorTypeButtonInBookingFragment.text = mentorTypeSelected
            binding.mentorNameButtonInBookingFragment.text = mentorNameSelected
            binding.problemDescriptionInputInBookingFragment.isEnabled = false
            binding.mentorTypeButtonInBookingFragment.isEnabled = false
            mentorID = sharedViewModel.reschedulingAppointment.mentorID.toString()
            binding.mentorNameButtonInBookingFragment.isEnabled = false
            binding.problemDescriptionInputInBookingFragment.setText(sharedViewModel.reschedulingAppointment.problemDescription)
            binding.cancelButtonInBookingFragment.setOnClickListener {
                findNavController().navigate(R.id.studentDashBoardFragment)
            }
        }
        binding.mentorTypeButtonInBookingFragment.setOnClickListener{
            val mentorTypesCoroutineScope = CoroutineScope(Dispatchers.Main)
            mentorTypesCoroutineScope.launch {

                var mentorTypes = MentorsAccess(requireContext(),sharedViewModel.currentInstitution.username!!).getMentorTypes()
                mentorTypesCoroutineScope.cancel()
            if (mentorTypes != null) {
                val mentorTypeBuilder = AlertDialog.Builder(context)
                mentorTypeBuilder.setTitle("Choose Mentor Type")
                mentorTypeBuilder.setSingleChoiceItems(
                    mentorTypes.map { it }.toTypedArray(),
                    0
                ) { dialog, selectedIndex ->
                    mentorTypeSelected = mentorTypes[selectedIndex].toString()
                    binding.mentorTypeButtonInBookingFragment.text = mentorTypeSelected
                    mentorTypes.clear()
//                            mentorTypes.removeObservers(viewLifecycleOwner)
                    dialog.dismiss()
                }
                mentorTypeBuilder.setPositiveButton("Go") { dialog, which ->
                    mentorTypeSelected = mentorTypes[0].toString()
                    binding.mentorTypeButtonInBookingFragment.text = mentorTypeSelected
                    mentorTypes.clear()
//                            mentorTypesLive.removeObservers(viewLifecycleOwner)
                    dialog.dismiss()
                }
                mentorTypeBuilder.create().show()
            }
            }
        }
        binding.mentorNameButtonInBookingFragment.setOnClickListener {
            if (mentorTypeSelected != "NA") {
                var mentorNamesCoroutineScope = CoroutineScope(Dispatchers.Main)
                mentorNamesCoroutineScope.launch {
                    var mentors = MentorsAccess(
                        requireContext(),
                        sharedViewModel.currentInstitution.username!!
                    ).getMentorNames(mentorTypeSelected)
                    mentorNamesCoroutineScope.cancel()

                    if (mentors != null) {
                        val mentorNameBuilder = AlertDialog.Builder(context)
                        mentorNameBuilder.setTitle("Choose Mentor Name")
                        mentorNameBuilder.setSingleChoiceItems(
                            mentors.map { it.name }.toTypedArray(),
                            0
                        ) { dialog, selectedIndex ->
                            mentorNameSelected = mentors[selectedIndex].name.toString()
                            mentorID = mentors[selectedIndex].username.toString()
                            binding.mentorNameButtonInBookingFragment.text = mentorNameSelected
                            mentors.clear()
                            dialog.dismiss()
                        }
                        mentorNameBuilder.setPositiveButton("Go") { dialog, which ->
                            mentorNameSelected = mentors[0].name
                            mentorID = mentors[0].username.toString()
                            binding.mentorNameButtonInBookingFragment.text = mentorNameSelected
                            mentors.clear()
                            dialog.dismiss()
                        }
                        mentorNameBuilder.create().show()
                    } else {
                        Toast.makeText(
                            context,
                            "Some error occurred in getting mentors",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            }
        }
        binding.bookingDateButtonInBookingFragment.setOnClickListener{
            if(mentorTypeSelected == "NA") {
                Toast.makeText(context,"Select mentor type first",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(mentorNameSelected == "NA"){
                Toast.makeText(context,"Select mentor name first",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val calendar = Calendar.getInstance()

            val datePickerDialog = context?.let { it1 ->
                DatePickerDialog(it1, { _, year, month, day ->
                    var monthToSet = month + 1
                    if(monthToSet < 10) selectedDate = "$day-0${monthToSet}-$year"
                    else selectedDate = "$day-${monthToSet}-$year"
                    binding.bookingDateButtonInBookingFragment.setText(selectedDate)
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }
            if (datePickerDialog != null) {
                datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            }
            datePickerDialog?.show()
        }
        var selectedTimeSlot = "NA"
        binding.bookingTimeSlotButtonInBookingFragment.setOnClickListener {
            var totalTimeSlots = arrayListOf<String>("9-10","10-11","11-12","1-2","2-3","3-4","4-5")
            var mentorTimeSlots = arrayListOf<String>()
            var availableTimeSlots = arrayListOf<String>()
            if (selectedDate != "") {
                reference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var dateRef = mentorTypeSelected+"/"+mentorID+"/appointments/"+selectedDate
                        Log.d("reschedule",dateRef)
                        if (snapshot.hasChild(dateRef)) {
                                for (timeSlots in snapshot.child(dateRef).children) {
                                    mentorTimeSlots.add(timeSlots.key.toString())
                                    Log.d("totalAdded",timeSlots.key.toString())
                                }
//                            if(sharedViewModel.rescheduling) if(sharedViewModel.reschedulingAppointment.date == selectedDate) if(mentorTimeSlots.contains(sharedViewModel.reschedulingAppointment.timeSlot)) mentorTimeSlots.remove(sharedViewModel.reschedulingAppointment.timeSlot)
                                for (timeslot in totalTimeSlots) {
                                    if (!mentorTimeSlots.contains(timeslot)) {
                                        availableTimeSlots.add(timeslot)
                                        Log.d("availableAdded",timeslot)
                                    }
                                }

                        }else{
                            availableTimeSlots = totalTimeSlots
                        }
                        if(!availableTimeSlots.isEmpty()) {
                            var timeSlotDialog = AlertDialog.Builder(context)
                            timeSlotDialog.setTitle("Select the Time")

                            timeSlotDialog.setSingleChoiceItems(
                                availableTimeSlots.toTypedArray(),
                                -1
                            ) { dialog, selectedIndex ->
                                selectedTimeSlot = availableTimeSlots[selectedIndex]
                                binding.bookingTimeSlotButtonInBookingFragment.text =
                                    selectedTimeSlot
                                availableTimeSlots.clear()
                                dialog.dismiss()
                            }
                            timeSlotDialog.setPositiveButton("Ok") { dialog, which ->
                                selectedTimeSlot = availableTimeSlots[0]
                                binding.bookingTimeSlotButtonInBookingFragment.text =
                                    selectedTimeSlot
                                availableTimeSlots.clear()
                                dialog.dismiss()
                            }
                            timeSlotDialog.create().show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })

            } else {
                Toast.makeText(context, "Select the date first", Toast.LENGTH_SHORT).show()
            }
        }
        binding.confirmBookingInBookingFragment.setOnClickListener {
            var problemDescription = binding.problemDescriptionInputInBookingFragment.text.toString()
            mentorNameSelected = mentorNameSelected.toString()

            if(mentorTypeSelected == "NA"){
                binding.mentorTypeButtonInBookingFragment.error = "Select type"
                binding.mentorTypeButtonInBookingFragment.requestFocus()
                return@setOnClickListener
            }
            if(mentorNameSelected == "NA" || mentorNameSelected.isEmpty()){
                binding.mentorNameButtonInBookingFragment.error = "Select mentor"
                binding.mentorNameButtonInBookingFragment.requestFocus()
                return@setOnClickListener
            }
            if(problemDescription.isEmpty()){
                binding.problemDescriptionInputInBookingFragment.error = "Write problem description first"
                binding.problemDescriptionInputInBookingFragment.requestFocus()
                return@setOnClickListener
            }

            if(sharedViewModel.rescheduling){
                var appointment = Appointment(
                    date = selectedDate,
                    timeSlot = selectedTimeSlot,
                    mentorType = mentorTypeSelected,
                    mentorID = mentorID,
                    studentID = sharedViewModel.currentUserID,
                    mentorName = mentorNameSelected,
                    studentName = sharedViewModel.currentStudent.name,
                    completed = false,
                    status = "Rescheduled",
                    remarks = "NA",
                    cancelled = false,
                    expanded = false,
                    problemDescription = problemDescription
                )
                    reschedule(appointment)

            }else{
                var appointment = Appointment(
                    date = selectedDate,
                    timeSlot = selectedTimeSlot,
                    mentorType = mentorTypeSelected,
                    mentorID = mentorID,
                    studentID = sharedViewModel.currentUserID,
                    mentorName = mentorNameSelected,
                    completed = false,
                    studentName = sharedViewModel.currentStudent.name,
                    status = "Booked",
                    remarks = "NA",
                    cancelled = false,
                    expanded = false,
                    problemDescription = problemDescription
                )
                createBooking(appointment)
                }
            }

        binding.cancelButtonInBookingFragment.setOnClickListener {
            findNavController().navigate(R.id.studentDashBoardFragment)
        }

        return binding.root
    }

    private fun reschedule(appointment: Appointment) {
        var rescheduleCoroutineScope = CoroutineScope(Dispatchers.Main)
        rescheduleCoroutineScope.launch {
            var rescheduled = BookingAccess(requireContext(),sharedViewModel).rescheduleAppointment(appointment)
            rescheduleCoroutineScope.cancel()
            if(!rescheduled){
                Toast.makeText(context,"Some error occurred in rescheduling booking",Toast.LENGTH_SHORT).show()

            }else{
                sharedViewModel.rescheduling =false
                findNavController().navigate(R.id.studentDashBoardFragment)
            }

        }
    }

    private fun createBooking(appointment: Appointment) {
        Log.d("createBooking"," in create booking")
        var bookCoroutineScope = CoroutineScope(Dispatchers.Main)
        bookCoroutineScope.launch {
            var bookingSuccess =
                BookingAccess(requireContext(), sharedViewModel).bookAppointment(appointment)
            bookCoroutineScope.cancel()
            if (bookingSuccess) {
                Toast.makeText(context, "Booked", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.studentDashBoardFragment)
            } else {
                Toast.makeText(context, "Some error occurred.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

}