package com.nitc.projectsgc.admin

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitc.projectsgc.models.Mentor
import com.nitc.projectsgc.R
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.admin.access.AddMentorAccess
import com.nitc.projectsgc.databinding.FragmentAddMentorBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddMentorFragment : Fragment() {
//    var database : FirebaseDatabase =
    lateinit var binding: FragmentAddMentorBinding
    val sharedViewModel:SharedViewModel by activityViewModels()
    var mentorNumber = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddMentorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var mentorTypeSelected = "NA"
//        addMentorBinding.mentorTypeButtonInAddMentorFragment.setOnClickListener {
//            val mentorTypesLive = context?.let { it1 -> MentorsAccess(it1,sharedViewModel.username).getMentorTypes() }
//            mentorTypesLive?.observe(viewLifecycleOwner) { mentorTypes ->
//                if (mentorTypes != null) {
//                    val mentorTypeBuilder = AlertDialog.Builder(context)
//                    mentorTypeBuilder.setTitle("Choose Mentor Type")
//                    mentorTypeBuilder.setSingleChoiceItems(
//                        mentorTypes.map { it }.toTypedArray(),
//                        mentorNumber
//                    ) { dialog, selectedIndex ->
//                        mentorTypeSelected = mentorTypes[selectedIndex].toString()
//                        addMentorBinding.mentorTypeButtonInAddMentorFragment.text = mentorTypeSelected
//                        mentorNumber = selectedIndex
//                        mentorTypes.clear()
//                        dialog.dismiss()
//                    }
//                    mentorTypeBuilder.setPositiveButton("Go") { dialog, which ->
//                        mentorTypeSelected = mentorTypes[0].toString()
//                        addMentorBinding.mentorTypeButtonInAddMentorFragment.text = mentorTypeSelected
//                        mentorTypes.clear()
//                        dialog.dismiss()
//                    }
//                    mentorTypeBuilder.create().show()
//                }
//
//            }
//        }
        binding.addButtonInAddMentorFragment.setOnClickListener{
            val nameOfMentor = binding.nameFieldInAddMentorFragment.text.toString()
            var phoneNumberOfMentor = binding.phoneNumberInAddMentorFragment.text.toString()
            var emailOfMentor = binding.emailFieldInAddMentorFragment.text.toString()
            var passwordOfMentor = binding.passwordFieldInAddMentorFragment.text.toString()

            if(nameOfMentor.isEmpty()){
                binding.nameFieldInAddMentorFragment.error = "Name field cannot be empty"
                binding.nameFieldInAddMentorFragment.requestFocus()
                return@setOnClickListener
            }
            if(emailOfMentor.isEmpty()){
                binding.emailFieldInAddMentorFragment.error = "Email field cannot be empty"
                binding.emailFieldInAddMentorFragment.requestFocus()
                return@setOnClickListener
            }
            emailOfMentor = emailOfMentor.lowercase()
            val institutionCoroutineScope = CoroutineScope(Dispatchers.Main)
            val domain : String = emailOfMentor.substring(emailOfMentor.indexOf("@")+1,emailOfMentor.length)
            institutionCoroutineScope.launch {
                institutionCoroutineScope.cancel()
                if (sharedViewModel.userType == "Admin" && domain.replace(
                        '.',
                        '_'
                    ) == sharedViewModel.currentInstitution.username) {

                    if (passwordOfMentor.length < 8) {
                        binding.passwordFieldInAddMentorFragment.error =
                            "Password should contain at least 8 characters"
                        binding.passwordFieldInAddMentorFragment.requestFocus()
                        return@launch
                    }

                    phoneNumberOfMentor = phoneNumberOfMentor.trim()
                    if (phoneNumberOfMentor.length < 10 || phoneNumberOfMentor.length > 10) {
                        phoneNumberOfMentor.trim()
                        binding.phoneNumberInAddMentorFragment.error =
                            "Phone number should be 10 digits only"
                        binding.phoneNumberInAddMentorFragment.requestFocus()
                        return@launch
                    } else if (!isDigitsOnly(phoneNumberOfMentor)) {
                        binding.phoneNumberInAddMentorFragment.setText("")
                        Toast.makeText(
                            context,
                            "Oops !! you entered phone number in wrong format",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.phoneNumberInAddMentorFragment.requestFocus()
                        return@launch
                    }

                    mentorTypeSelected =
                        binding.mentorTypeInputInAddMentorFragment.text.toString().trim()
                    if (mentorTypeSelected.isEmpty()) {
                        binding.mentorTypeInputInAddMentorFragment.error =
                            "Add Mentor type"
                        binding.mentorTypeInputInAddMentorFragment.requestFocus()
                        return@launch
                    }
                    val userName = emailOfMentor.substring(0, emailOfMentor.indexOf('@')).replace(Regex("[^a-zA-Z0-9]+"),"_")
                    Log.d("addMentor",userName)
                    val mentor = Mentor(
                        nameOfMentor,
                        phoneNumberOfMentor,
                        emailOfMentor,
                        mentorTypeSelected,
                        passwordOfMentor,
                        userName
                    )

                    val coroutineScope = CoroutineScope(Dispatchers.Main)
                    val loadingDialog = Dialog(requireContext())
                    loadingDialog.setContentView(
                        requireActivity().layoutInflater.inflate(
                            R.layout.loading_dialog,
                            null
                        )
                    )
                    loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    coroutineScope.launch {
                        loadingDialog.create()
                        loadingDialog.show()
                        val addMentorSuccess =
                            AddMentorAccess(
                                requireContext(),
                                sharedViewModel.currentInstitution.username!!
                            ).addMentor(mentor)

                        loadingDialog.cancel()
                        coroutineScope.cancel()
                        if (addMentorSuccess) {
                            findNavController().navigate(R.id.adminDashboardFragment)
                            Toast.makeText(context, "Added", Toast.LENGTH_SHORT).show()
                        }
                    }
                }else{
                    binding.emailFieldInAddMentorFragment.error =
                        "Email should be your institution valid email"
                    binding.emailFieldInAddMentorFragment.requestFocus()
                    return@launch
                }
            }

        }

    }
//    private fun checkDomain(emailInput: String): Boolean {
//        val institutionCoroutineScope = CoroutineScope(Dispatchers.Main)
//        val domain : String = emailInput.substring(emailInput.indexOf("@")+1,emailInput.length)
//        var checked = false
//        institutionCoroutineScope.launch {
//            val institution = InstitutionAccess(requireContext(),sharedViewModel).getInstitution(domain.replace(Regex("[^a-zA-Z0-9]+"),"_"))
//            institutionCoroutineScope.cancel()
//            if(institution != null){
//                checked = true
//            }
//        }
//        return checked
//    }
    private fun isDigitsOnly(str: String): Boolean {
        return str.matches(Regex("[0-9]+"))
    }
}