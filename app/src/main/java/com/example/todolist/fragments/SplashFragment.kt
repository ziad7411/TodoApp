package com.example.todolist.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.todolist.R
import com.google.firebase.auth.FirebaseAuth


class SplashFragment : Fragment() {


lateinit var auth: FirebaseAuth
lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        navController = Navigation.findNavController(view)
        val isLogin: Boolean = auth.currentUser != null

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed({


                navController.navigate(R.id.action_splashFragment_to_signInFragment)

        }, 2000)
    }


}