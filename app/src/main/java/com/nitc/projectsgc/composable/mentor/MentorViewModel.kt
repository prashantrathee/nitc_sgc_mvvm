package com.nitc.projectsgc.composable.mentor

import androidx.lifecycle.ViewModel
import com.nitc.projectsgc.composable.mentor.repo.MentorRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MentorViewModel @Inject constructor(private val mentorRepo: MentorRepo) :ViewModel() {



}