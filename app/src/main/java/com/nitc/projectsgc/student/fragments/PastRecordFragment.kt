package com.nitc.projectsgc.student.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.databinding.FragmentPastRecordBinding
import com.nitc.projectsgc.mentors.access.MentorAppointmentsAccess
import com.nitc.projectsgc.mentors.adapters.PastRecordAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class PastRecordFragment:Fragment() {

    private val sharedViewModel:SharedViewModel by activityViewModels()
    lateinit var binding:FragmentPastRecordBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPastRecordBinding.inflate(inflater,container,false)

        var pastAppointmentsCoroutineScope = CoroutineScope(Dispatchers.Main)
        pastAppointmentsCoroutineScope.launch {
            var pastRecord = MentorAppointmentsAccess(
                requireContext(),
                sharedViewModel = sharedViewModel
            ).getStudentRecord(sharedViewModel.pastRecordStudentID)
            pastAppointmentsCoroutineScope.cancel()
            binding.pastRecordRecyclerViewInPastRecordFragment.layoutManager =
                LinearLayoutManager(context)
                    if (pastRecord != null) {
                        binding.pastRecordRecyclerViewInPastRecordFragment.adapter =
                            context?.let {
                                PastRecordAdapter(
                                    it,
                                    this@PastRecordFragment,
                                    pastRecord,
                                    sharedViewModel = sharedViewModel
                                )
                            }
                    }
        }

        return binding.root
    }

}