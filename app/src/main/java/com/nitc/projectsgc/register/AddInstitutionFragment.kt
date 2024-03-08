package com.nitc.projectsgc.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitc.projectsgc.InstitutionsAccess
import com.nitc.projectsgc.models.Admin
import com.nitc.projectsgc.models.Institution
import com.nitc.projectsgc.R
import com.nitc.projectsgc.SharedViewModel
import com.nitc.projectsgc.databinding.FragmentAddInstitutionBinding
import com.nitc.projectsgc.register.access.RegisterAccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class AddInstitutionFragment: Fragment() {

    private val sharedViewModel:SharedViewModel by activityViewModels()
    lateinit var binding:FragmentAddInstitutionBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddInstitutionBinding.inflate(inflater,container,false)


        if(sharedViewModel.userType == 0){

        }
        binding.createButtonInAddInstitutionFragment.setOnClickListener {
            val institutionName = binding.nameInputInAddInstitutionFragment.text.toString()
            if(institutionName.isBlank()){
                binding.nameInputInAddInstitutionFragment.error = "Enter Institution Name first"
                binding.nameInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            if(!verifyName(institutionName)){
                binding.nameInputInAddInstitutionFragment.error = "Institution name should not contain special symbols"
                binding.nameInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            if(institutionName[0].isDigit()){
                binding.nameInputInAddInstitutionFragment.error = "Institution name should start with an alphabet"
                binding.nameInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            var emailSuffix = binding.suffixInputInAddInstitutionFragment.text.toString()
            if(emailSuffix.isBlank()){
                binding.suffixInputInAddInstitutionFragment.error = "Enter Institution Name first"
                binding.suffixInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            val verifiedSuffix = verifySuffix(emailSuffix)
            if(!verifiedSuffix){
                binding.suffixInputInAddInstitutionFragment.error = "Suffix should be of form - 'in' or 'com'"
                binding.suffixInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            var adminEmail = binding.adminEmailInputInAddInstitutionFragment.text.toString()
            if(adminEmail.isBlank()){
                binding.adminEmailInputInAddInstitutionFragment.error = "Enter Institution Name first"
                binding.adminEmailInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            adminEmail = adminEmail.lowercase()
            if(!verifyEmail(adminEmail,emailSuffix)){
                binding.adminEmailInputInAddInstitutionFragment.error = "Enter valid email"
                binding.adminEmailInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            val adminPassword = binding.adminPasswordInputInAddInstitutionFragment.text.toString()
            if(adminPassword.isBlank()){
                binding.adminPasswordInputInAddInstitutionFragment.error = "Enter Institution Name first"
                binding.adminPasswordInputInAddInstitutionFragment.requestFocus()
                return@setOnClickListener
            }
            val institution = Institution(
                institutionName,
                institutionName.replace(' ','_'),
                emailSuffix
            )
            val username = adminEmail.substring(0,adminEmail.indexOf("@"))
            val admin = Admin(username,adminEmail,adminPassword)
            addInstitution(institution,admin)
        }

        return binding.root
    }

    private fun addInstitution(institution: Institution, admin: Admin) {

        val institutionsAccess = InstitutionsAccess(
            requireContext(),
            sharedViewModel
        )
        val institutionAddCoroutineScope = CoroutineScope(Dispatchers.Main)
        institutionAddCoroutineScope.launch {
            val institutionAdded = institutionsAccess.addInstitution(institution)
            institutionAddCoroutineScope.cancel()
            if (institutionAdded) {
                val adminCoroutineScope = CoroutineScope(Dispatchers.Main)
                adminCoroutineScope.launch {
                    val adminAdded = RegisterAccess(
                        requireContext()
                    ).addAdmin(admin, institution.username!!)
                    adminCoroutineScope.cancel()
                    if (adminAdded) {
                        Toast.makeText(
                            context,
                            "Institution Added. Verify email and login as Admin",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.loginFragment)
                    }else{
                        var deleteInstitutionCoroutineScope = CoroutineScope(Dispatchers.Main)
                        deleteInstitutionCoroutineScope.launch {
                            institutionsAccess.deleteInstitution(institution.username!!)
                            deleteInstitutionCoroutineScope.cancel()
                        }
                    }
                }
            }
        }
    }

    private fun verifyEmail(adminEmail: String, emailSuffix: String): Boolean {
            val domain : String = adminEmail.substring(adminEmail.indexOf("@")+1,adminEmail.length)
            return domain == emailSuffix
    }

    private fun verifyName(institutionName:String):Boolean{
        val regex = Regex("^[a-zA-Z0-9]*$")
        return regex.matches(institutionName)
    }

    private fun verifySuffix(emailSuffix:String):Boolean {
        Log.d("emailSuffix",emailSuffix.substring(emailSuffix.length-2,emailSuffix.length))
        return (emailSuffix.substring(emailSuffix.length-2,emailSuffix.length) == "in" || emailSuffix.substring(emailSuffix.length-3,emailSuffix.length) == "com")
    }

}