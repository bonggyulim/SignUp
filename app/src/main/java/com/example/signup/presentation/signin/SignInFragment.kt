package com.example.signup.presentation.signin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentSignInBinding
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.signup.SignUpFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        initView()
//        observeSignIn()
//        observeCurrentUser()
    }

    private fun initView() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.signIn(email, password)
        }
        binding.btnSignUp.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .replace(R.id.frameLayout, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun observeSignIn() {
        lifecycleScope.launch {
            viewModel.signInState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        //No action needed
                    }
                    is UiState.Success -> {
                        Toast.makeText(requireActivity(), "로그인 성공", Toast.LENGTH_SHORT).show()
                        replaceFragment(HomeFragment())
                    }
                    is UiState.Error -> {

                    }
                }
            }
        }
    }

    private fun observeCurrentUser() {
        viewModel.getCurrentUser()
        lifecycleScope.launch {
            viewModel.currentUserState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> replaceFragment(HomeFragment())
                    is UiState.Error -> {

                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}