package com.nitc.projectsgc.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.text.isDigitsOnly
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitc.projectsgc.CircleLoadingDialog
import com.nitc.projectsgc.InstitutionsAccess
import com.nitc.projectsgc.models.Institution
import com.nitc.projectsgc.R
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.models.Student
import com.nitc.projectsgc.databinding.FragmentRegisterBinding
import com.nitc.projectsgc.register.access.RegisterAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Calendar.DAY_OF_MONTH
import java.util.Calendar.MONTH
import java.util.Calendar.YEAR

class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener,CircleLoadingDialog {
    lateinit var binding: FragmentRegisterBinding
    lateinit var studentGender:Spinner
    private val sharedViewModel:SharedViewModel by activityViewModels()
    lateinit var selectedGenderTextView: String

//    creating database reference object
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater,container,false)
        val arrayAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.gender_options,
            android.R.layout.simple_spinner_item
        )
        if(sharedViewModel.userType == 0){
            binding.registerAsInstitutionButtonInRegisterFragment.visibility = View.GONE
            binding.headingTVInRegisterFragment.text = "Add Student"
            binding.signUpButtonInRegisterFragment.text = "Add"
        }else if(sharedViewModel.userType != 2){
            binding.registerAsInstitutionButtonInRegisterFragment.visibility = View.VISIBLE
            binding.headingTVInRegisterFragment.text = "Register"
            binding.signUpButtonInRegisterFragment.text = "Register"
        }
        studentGender = binding.genderSpinnerInRegisterFragment
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        studentGender.adapter = arrayAdapter
        studentGender.onItemSelectedListener = this
        var dateOfBirth = ""
        val calendar = Calendar.getInstance()
        binding.dateOfBirthButtonInRegisterFragment.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                {DatePicker,year:Int,monthOfYear:Int,dayOfMonth:Int->
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
                    val dateToday = Calendar.getInstance()
                    dateToday.set(year,monthOfYear,dayOfMonth)
                    dateOfBirth = dateFormat.format(dateToday.time)
                    binding.dateOfBirthButtonInRegisterFragment.text = dateOfBirth
                },
                calendar.get(YEAR),
                calendar.get(MONTH),
                calendar.get(DAY_OF_MONTH)
            ).show()
        }
        binding.signUpButtonInRegisterFragment.setOnClickListener{
            val nameInput = binding.nameFieldInRegisterFragment.text.toString()
            val emailInput = binding.emailFieldInRegisterFragment.text.toString()
            val passwordInput = binding.passwordFieldInRegisterFragment.text.toString()
            var phoneNumber = binding.phoneNumberInRegisterFragment.text.toString()


            if(nameInput.isEmpty()){
                binding.nameFieldInRegisterFragment.error = "Name field cannot be empty"
                binding.nameFieldInRegisterFragment.requestFocus()
                return@setOnClickListener
            }
            var names = nameInput.toString().trim().split(" ")
            var nameValid = true
            for (name in names){
                if(!isAlphabetic(name)){
                    nameValid = false
                    break
                }
            }
            if(!nameValid){
                binding.nameFieldInRegisterFragment.error = "Name field shouldn't contain numbers"
                binding.nameFieldInRegisterFragment.requestFocus()
                return@setOnClickListener
            }
            if(emailInput.isEmpty()){
                binding.emailFieldInRegisterFragment.error = "Email field cannot be empty"
                binding.emailFieldInRegisterFragment.requestFocus()
                return@setOnClickListener
            }
            val institutionCoroutineScope = CoroutineScope(Dispatchers.Main)
            val count = emailInput.count{it=='@'}
            if(count != 1) {
                binding.emailFieldInRegisterFragment.error = "Enter a valid email"
                binding.emailFieldInRegisterFragment.requestFocus()
                return@setOnClickListener
            }
            val domain : String = emailInput.substring(emailInput.indexOf("@")+1,emailInput.length)



            institutionCoroutineScope.launch {
                var institution: Institution? = null
                if(sharedViewModel.userType != 0) institution = InstitutionsAccess(requireContext(),sharedViewModel).getInstitution(domain.replace(Regex("[^a-zA-Z0-9]+"),"_"))
            institutionCoroutineScope.cancel()
                if(institution != null || (sharedViewModel.userType == 0 && domain.replace(Regex("[^a-zA-Z0-9]+"),"_") == sharedViewModel.currentInstitution.username)){
                    Log.d("checkDomain","checked = true")
                    if(passwordInput.length < 8){
                        binding.passwordFieldInRegisterFragment.error = "Password should contain at least 8 characters"
                        binding.passwordFieldInRegisterFragment.requestFocus()
//                        return@setOnClickListener
                        return@launch
                    }

                    phoneNumber = phoneNumber.trim()
                    if(phoneNumber.length <10 || phoneNumber.length >10){
                        phoneNumber.trim()
                        binding.phoneNumberInRegisterFragment.error = "Phone number should be 10 digits only"
                        binding.phoneNumberInRegisterFragment.requestFocus()
//                        return@setOnClickListener
                        return@launch
                    }else if (!isDigitsOnly(phoneNumber)){
                        binding.phoneNumberInRegisterFragment.setText("")
                        Toast.makeText(context,"Oops !! you entered phone number in wrong format", Toast.LENGTH_LONG).show()
                        binding.phoneNumberInRegisterFragment.requestFocus()
//                        return@setOnClickListener
                        return@launch
                    }

//            val checked = binding.tncBoxInRegisterFragment
//            if(!checked.isChecked){
//                Toast.makeText(context,"You have to accept the terms and conditions", Toast.LENGTH_LONG).show()
//                return@setOnClickListener
//            }

//
//            Toast.makeText(context,"Email = $emailInput \n" +
//                    " password = $passwordInput \n"+
//                    "phone = $phoneNumber \n"+
//                    "name = $nameInput \n"+
//                    "D.O.B = $dateOfBirth\n"+
//                    "Gender = $selectedGenderTextView \n"
//                , Toast.LENGTH_LONG).show()

                    var rollNo = binding.rollNoFieldInRegisterFragment.text.toString()
                    if(rollNo.isEmpty()){
                        binding.rollNoFieldInRegisterFragment.error = "Enter roll number first"
                        binding.rollNoFieldInRegisterFragment.requestFocus()
//                        return@setOnClickListener
                        return@launch
                    }
                    rollNo = rollNo.lowercase()
//            if(!verifyRollNO(rollNo)){
//                binding.emailFieldInRegisterFragment.error = "You have entered the roll no. in email incorrectly"
//                binding.emailFieldInRegisterFragment.requestFocus()
//                return@setOnClickListener
//            }

                    val loadingDialog = getLoadingDialog(requireActivity(),requireContext())
                    Log.d("today","this called")
                    val registerCoroutineScope = CoroutineScope(Dispatchers.Main)
                    val username = emailInput.substring(0,emailInput.indexOf('@')).replace(Regex("[^a-zA-Z0-9]+"),"_")
                    registerCoroutineScope.launch {
                        val student = Student(
                            rollNo,
                            nameInput,
                            username,
                            dateOfBirth,
                            emailInput,
                            selectedGenderTextView,
                            passwordInput,
                            phoneNumber
                        )
                        loadingDialog.create()
                        loadingDialog.show()
                        val registerSuccess = RegisterAccess(requireContext()).register(student,sharedViewModel.currentInstitution.username.toString())
                        loadingDialog.cancel()
                        registerCoroutineScope.cancel()
                        if(registerSuccess){
                            if(sharedViewModel.userType == 0) findNavController().navigate(R.id.adminDashboardFragment)
                            else findNavController().navigate(R.id.loginFragment)
                        }else{
                            Toast.makeText(context,"Error in registration",Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                    }

                }else {
                    binding.emailFieldInRegisterFragment.error = "Email should be connected with an institution"
                    binding.emailFieldInRegisterFragment.requestFocus()
                    return@launch
                }
            }




//            findNavController().navigate(R.id.bookingFragment)

        }

        binding.registerAsInstitutionButtonInRegisterFragment.setOnClickListener {
            findNavController().navigate(R.id.addInstitutionFragment)
        }

//        registerBinding.f
    val backCallback = object : OnBackPressedCallback(true /* enabled by default */) {
        override fun handleOnBackPressed() {
            // Call a method in your Fragment to handle the navigation
            if(sharedViewModel.userType == 0) findNavController().navigate(R.id.adminDashboardFragment)
            else requireActivity().finish()
        }
    }
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backCallback)
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun verifyRollNO(rollNo: String): Boolean {
        if(rollNo.length != 9)return false
        if(rollNo[0]!='p' && rollNo[0]!='m' && rollNo[0]!='b')return false
        val streamId = rollNo.substring(7)
        if(streamId.isDigitsOnly())return false
        if(streamId != streamId.lowercase())return false
        return true
    }

    suspend fun checkDomain(emailInput: String,callback:(Boolean)->Unit) {

//            Log.d("checkDomain","returning checked = $checked")

        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize UI elements and set up event listeners here

    }
    fun isDigitsOnly(str: String): Boolean {
        return str.matches(Regex("[0-9]+"))
    }
    fun isAlphabetic(name: String): Boolean {
        return name.matches("[a-zA-Z]+".toRegex())
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            selectedGenderTextView = parent.getItemAtPosition(position).toString()
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}
