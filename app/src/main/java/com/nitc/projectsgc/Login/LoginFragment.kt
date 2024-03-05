package com.nitc.projectsgc.Login

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitc.projectsgc.InstitutionsAccess
import com.nitc.projectsgc.Login.access.LoginAccess
import com.nitc.projectsgc.R
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.admin.access.MentorsAccess
import com.nitc.projectsgc.databinding.FragmentLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    var userType = "Student"
    var mentorNumber = 0
    var mentorTypeSelected = "NA"
    private val sharedViewModel:SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loadingDialog = Dialog(requireContext())
        loadingDialog.setContentView(requireActivity().layoutInflater.inflate(R.layout.loading_dialog,null))
        loadingDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        loadingDialog.create()

        binding.adminLoginTypeButtonInLoginFragment.setOnClickListener {
            binding.adminLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_blue_bg)
            binding.adminLoginTypeButtonInLoginFragment.setTextColor(Color.WHITE)
            binding.studentLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.studentLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
//            binding.mentorTypeButtonInLoginFragment.visibility = View.GONE
            binding.mentorTypeButtonInLoginFragment.visibility = View.GONE
            binding.mentorLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.mentorLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
            binding.editTextTextEmailAddress.setText("")
            binding.editTextTextPassword.setText("")
//            binding.signUpButton.visibility = View.GONE
            userType = "Admin"

        }
        binding.studentLoginTypeButtonInLoginFragment.setOnClickListener {
            binding.adminLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.adminLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
            binding.studentLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_blue_bg)
            binding.studentLoginTypeButtonInLoginFragment.setTextColor(Color.WHITE)
            binding.mentorTypeButtonInLoginFragment.visibility = View.GONE
            binding.mentorLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.mentorLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
            binding.editTextTextEmailAddress.setText("")
            binding.editTextTextPassword.setText("")
//            binding.signUpButton.visibility = View.VISIBLE
            userType = "Student"
        }
        binding.mentorLoginTypeButtonInLoginFragment.setOnClickListener {
            binding.adminLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.adminLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
            binding.studentLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_transparent_bg)
            binding.studentLoginTypeButtonInLoginFragment.setTextColor(Color.BLACK)
            binding.mentorTypeButtonInLoginFragment.visibility = View.VISIBLE
            binding.mentorLoginTypeImageInLoginFragment.setBackgroundResource(R.drawable.login_type_card_blue_bg)
            binding.mentorLoginTypeButtonInLoginFragment.setTextColor(Color.WHITE)
            binding.editTextTextEmailAddress.setText("")
            binding.editTextTextPassword.setText("")
//            binding.signUpButton.visibility = View.GONE
            userType = "Mentor"
        }

        binding.mentorTypeButtonInLoginFragment.setOnClickListener {
            val mentorTypesCoroutineScope = CoroutineScope(Dispatchers.Main)
            mentorTypesCoroutineScope.launch {
                val mentorTypes = MentorsAccess(
                        requireContext(),
                        sharedViewModel.currentInstitution.username!!
                    ).getMentorTypes()
//                mentorTypesLive?.observe(viewLifecycleOwner) { mentorTypes ->
                mentorTypesCoroutineScope.cancel()
                    if (mentorTypes != null) {
                        val mentorTypeBuilder = AlertDialog.Builder(context)
                        mentorTypeBuilder.setTitle("Choose Mentor Type")
                        mentorTypeBuilder.setSingleChoiceItems(
                            mentorTypes.map { it }.toTypedArray(),
                            mentorNumber
                        ) { dialog, selectedIndex ->
                            mentorTypeSelected = mentorTypes[selectedIndex].toString()
                            binding.mentorTypeButtonInLoginFragment.text = mentorTypeSelected
                            mentorNumber = selectedIndex
                            mentorTypes.clear()
                            dialog.dismiss()
                        }
                        mentorTypeBuilder.setPositiveButton("Go") { dialog, which ->
                            mentorTypeSelected = mentorTypes[0].toString()
                            binding.mentorTypeButtonInLoginFragment.text = mentorTypeSelected
                            mentorTypes.clear()
                            dialog.dismiss()
                        }
                        mentorTypeBuilder.create().show()
                    }

            }
        }
        binding.signInButton.setOnClickListener {
            var emailInput = requireView().findViewById<EditText>(R.id.editTextTextEmailAddress).text.toString()
            val passwordInput = requireView().findViewById<EditText>(R.id.editTextTextPassword).text.toString()
            emailInput = emailInput.trim()
            if(emailInput.isEmpty()){
                binding.editTextTextEmailAddress.error = "No email entered"
                binding.editTextTextEmailAddress.requestFocus()
                return@setOnClickListener
            }
            emailInput = emailInput.lowercase()
            if(passwordInput.isEmpty()){
                binding.editTextTextPassword.error = "No password entered"
                binding.editTextTextPassword.requestFocus()
                return@setOnClickListener
            }
            val loginAccess = LoginAccess(
                requireContext(),
                this@LoginFragment,
                sharedViewModel
            )

            val loginCoroutineScope = CoroutineScope(Dispatchers.Main)
            val emailDomain = emailInput.substring(emailInput.indexOf("@")+1,emailInput.length)
            val _index = emailInput.indexOf('_')
//            val indexOfAt = emailInput.indexOf('@')
//            if(_index)
            if(userType == "Student"){

//                val rollNo = emailInput.substring(emailInput.indexOf("_")+1,emailInput.indexOf("@"))
                val username = emailInput.substring(0,emailInput.indexOf('@'))
//                    val username = emailInput.replace('@','-').replace(Regex("[^a-zA-Z0-9]+"),"_")
                        loginCoroutineScope.launch {
                            loadingDialog.show()
                            val loginSuccess = loginAccess.login(
                                emailInput,
                                passwordInput,
                                userType,
                                username,
                                "NA",
                                emailDomain
                            )
                            loadingDialog.cancel()
                            loginCoroutineScope.cancel()
                            if (loginSuccess) {
                                sharedViewModel.currentUserID = username
                                sharedViewModel.userType = "Student"
                                Log.d("loginSuccess", loginSuccess.toString())
                                findNavController().navigate(R.id.studentDashBoardFragment)
                            }
                        }
            }
            else if(userType == "Admin"){
//                Toast.makeText(requireContext(),"Email entered is $emailInput and password entered is $passwordInput",Toast.LENGTH_LONG).show()

                loginCoroutineScope.launch {
                    loadingDialog.show()
                    val loginSuccess = loginAccess.login(
                        emailInput,
                        passwordInput,
                        userType,
                        emailInput,
                        "NA",
                        emailDomain
                    )
                    loadingDialog.cancel()
                    loginCoroutineScope.cancel()
                    if(loginSuccess){
                        sharedViewModel.userType = "Admin"
                        findNavController().navigate(R.id.adminDashboardFragment)
                    }
                }
            }
            else if(userType == "Mentor"){
                if(mentorTypeSelected == "NA"){
                    Toast.makeText(context,"Please select your type",Toast.LENGTH_SHORT).show()
                }
                val userName = emailInput.substring(0,emailInput.indexOf("@")).replace(Regex("[^a-zA-Z0-9]+"),"_")

                    loginCoroutineScope.launch {
                        loadingDialog.show()
                        val loginSuccess = loginAccess.login(
                            emailInput,passwordInput,userType,userName,mentorTypeSelected,emailDomain)
                        loadingDialog.cancel()
                        loginCoroutineScope.cancel()
                        if(loginSuccess == true){
                            sharedViewModel.currentUserID = userName
                            sharedViewModel.userType = "Mentor"
                            findNavController().navigate(R.id.mentorDashboardFragment)
                        }
                    }
            }
        }

        binding.signUpButton.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }
        binding.forgotPasswordButtonInLoginFragment.setOnClickListener {
            findNavController().navigate(R.id.forgotPasswordFragment)
        }
        val backCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // Call a method in your Fragment to handle the navigation
                requireActivity().finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,backCallback)
    }
}
