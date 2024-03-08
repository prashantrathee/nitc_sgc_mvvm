package com.nitc.projectsgc

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nitc.projectsgc.databinding.FragmentIntroductoryBinding
import com.nitc.projectsgc.models.Institution
import kotlinx.coroutines.*


class IntroductoryFragment : Fragment(),CircleLoadingDialog {

    private val sharedViewModel:SharedViewModel by activityViewModels()
    private lateinit var binding:FragmentIntroductoryBinding
    var selectedPosition = 0
    var institutionSelected:Institution? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentIntroductoryBinding.inflate(inflater,container,false)
        var sharedPreferences = requireActivity().getSharedPreferences("sgcLogin", Context.MODE_PRIVATE)
        val institutionUsername = sharedPreferences.getString("institutionUsername",null)
        if(institutionUsername != null) {


            Log.d("institutionusername", institutionUsername.toString())
            val institutionCoroutineScope = CoroutineScope(Dispatchers.Main)
            val loadingDialog = getLoadingDialog(requireActivity(),requireContext())
            institutionCoroutineScope.launch {
                loadingDialog.create()
                loadingDialog.show()
                val institution = InstitutionsAccess(
                    requireContext(),
                    sharedViewModel
                ).getInstitution(sharedPreferences.getString("institutionUsername", null)!!)
                loadingDialog.cancel()
                institutionCoroutineScope.cancel()
                if(institution!= null) sharedViewModel.currentInstitution = institution
            }
            if (sharedPreferences.getBoolean("loggedIn", false)) {
                when (sharedPreferences.getInt("userType",1)) {
                    1 -> {
                        findNavController().navigate(R.id.studentDashBoardFragment)
                    }

                    0 -> {
                        findNavController().navigate(R.id.adminDashboardFragment)
                    }

                    2 -> {
                        findNavController().navigate(R.id.mentorDashboardFragment)
                    }
                }
            }
        }else{
            binding.chooseInstitutionButtonInIntroductoryFragment.setOnClickListener {
                getInstitutions()
            }
            binding.continueButtonInIntroductoryFragment.setOnClickListener {
                if(institutionSelected == null){
                    binding.chooseInstitutionButtonInIntroductoryFragment.error = "Choose Institution first"
                    binding.chooseInstitutionButtonInIntroductoryFragment.requestFocus()
                    return@setOnClickListener
                }
                sharedViewModel.currentInstitution = institutionSelected!!
                findNavController().navigate(R.id.loginFragment)
            }
            binding.registerButtonInIntroductoryFragment.setOnClickListener{
                findNavController().navigate(R.id.addInstitutionFragment)
            }
        }
//        userTypeLive.observe(viewLifecycleOwner){userType->
//            if(userType == 1){
//                findNavController().navigate()
//            }else if(userType == 2){
//                findNavController().navigate(R.id.mentorUpdateFragment)
//            }else{
//                findNavController().navigate(R.id.loginFragment)
//            }
//        }

//        val backgroundExecutor = Executors.newSingleThreadScheduledExecutor()

//        coroutineScope.launch{
//            delay(SPLASH_SCREEN)
//
////            withContext(Dispatchers.IO){
////
////            }
//        }
        return binding.root
    }

    private fun getInstitutions() {
        val institutionsCoroutineScope = CoroutineScope(Dispatchers.Main)
        val loadingDialog = getLoadingDialog(requireActivity(),requireContext())
        institutionsCoroutineScope.launch {
            loadingDialog.create()
            loadingDialog.show()
            val institutions = InstitutionsAccess(
                requireContext(),
                sharedViewModel
            ).getInstitutions()
            loadingDialog.cancel()
            institutionsCoroutineScope.cancel()
            if(!institutions.isNullOrEmpty()){
                institutionSelected = institutions[0]
                val institutionsBuilder = AlertDialog.Builder(context)
                    .setTitle("Choose Institution")
                    .setSingleChoiceItems(institutions.map{it.institutionName}.toTypedArray(),0){dialog,selected->
                        institutionSelected = institutions[selected]
                        binding.chooseInstitutionButtonInIntroductoryFragment.text = institutionSelected!!.institutionName
                        binding.continueButtonInIntroductoryFragment.visibility = View.VISIBLE
                        selectedPosition = selected
                    }
                    .setPositiveButton("Select"){dialog,selected->
                        institutionSelected = institutions[selectedPosition]
                        binding.chooseInstitutionButtonInIntroductoryFragment.text = institutionSelected!!.institutionName
                        binding.continueButtonInIntroductoryFragment.visibility = View.VISIBLE
                        dialog.dismiss()
                    }
                    .setNegativeButton("Cancel"){dialog,which->
                        institutionSelected = null
                        binding.continueButtonInIntroductoryFragment.visibility = View.GONE
                        dialog.dismiss()
                    }
                institutionsBuilder.create().show()
            }else{
                if(institutions != null && institutions.isEmpty()){
                    Toast.makeText(context,"No Institution registered yet. Register your institution",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


}