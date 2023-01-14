package com.grumpy.pestcontrol.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseUser
import com.grumpy.pestcontrol.MainActivity
import com.grumpy.pestcontrol.R
import com.grumpy.pestcontrol.auth.AuthViewModel
import com.grumpy.pestcontrol.auth.AuthViewModelFactory
import com.grumpy.pestcontrol.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {


    private var _binding :FragmentSignInBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel: AuthViewModel




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, AuthViewModelFactory()).get(AuthViewModel::class.java)


        viewModel.getUserData().observe(this, Observer<FirebaseUser>(){ user ->
            if(user != null){
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_registerFragment)
        }

        binding.signInBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn(){
        val email = binding.emailEditSignIn.text.toString()
        val password = binding.passEditSignIn.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            viewModel.signIn(email,password)
        }

    }

}