package com.grumpy.pestcontrol.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.grumpy.pestcontrol.MainActivity
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.auth.AuthViewModel
import com.grumpy.pestcontrol.auth.AuthViewModelFactory
import com.grumpy.pestcontrol.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel : AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this,AuthViewModelFactory()).get(AuthViewModel::class.java)
        viewModel.getUserData().observe(this, Observer<FirebaseUser>(){ user ->
            if(user != null){
                findNavController().navigate(R.id.action_registerFragment_to_signInFragment)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signInText.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_signInFragment)
        }

        binding.signUpBtn.setOnClickListener {
            signup()
        }
    }


    //Todo: add form validation
    private fun signup(){
        val email = binding.emailEditSignUp.text.toString()
        val password = binding.passEditSignUp.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.register(email, password)
        }
    }


}