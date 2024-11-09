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
        initObserver()
    }
    private fun initView() {
        binding.btnSignUp.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val name = binding.etName.text.toString()
            viewModel.signUp(
                UserModel(
                    userName = name,
                    userEmail = email
                ), password)
        }

    }

    private fun initObserver() {
        lifecycleScope.launch {
            viewModel.signUpState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        //No action needed
                    }
                    is UiState.Success -> {
                        Toast.makeText(requireActivity(), "회원가입 성공", Toast.LENGTH_SHORT).show()
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