package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.example.todolist.databinding.FragmentSigninBinding
import com.example.todolist.databinding.FragmentSignupBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth


class SigninFragment : Fragment() {


    private lateinit var mAuth: FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var binding: FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSigninBinding.inflate(inflater,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
init(view)
        registerEvents()
    }

    private fun init(view: View){
        navController = Navigation.findNavController(view)
        mAuth = FirebaseAuth.getInstance()
    }
    private fun registerEvents(){

        binding.textViewSignUp.setOnClickListener{
            navController.navigate(R.id.action_signInFragment_to_signUpFragment)
        }
        binding.nextBtn.setOnClickListener{
            val email = binding.emailEt.text.toString()
            val pass = binding.passEt.text.toString()

            if (email.isNotEmpty() &&  pass.isNotEmpty()){
                mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener{
                        if (it.isSuccessful){
                            Toast.makeText(context,"Login Successful", Toast.LENGTH_LONG).show()
                            navController.navigate(R.id.action_signInFragment_to_homeFragment)
                        }else{
                            Toast.makeText(context,it.exception?.message, Toast.LENGTH_LONG).show()

                        }

                    }
                }else{
                Toast.makeText(context,"Empty fields not Allowed", Toast.LENGTH_LONG).show()


            }
        }
    }
}