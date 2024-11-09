package com.example.signup.presentation.signup

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
import com.example.signup.databinding.FragmentSignUpBinding
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.model.UserModel
import com.example.signup.presentation.phoneAuth.PhoneAuthFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeSignUp()
    }
    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.signUp(email, password)
        }

    }

    private fun observeSignUp() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        //No action needed
                    }
                    is UiState.Success -> {
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, HomeFragment())
                            .commit()
                    }
                    is UiState.Error -> {
                        Log.d("firebase", state.toString())
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}