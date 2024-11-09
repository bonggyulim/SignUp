package com.example.signup.presentation.phoneAuth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentPhoneAuthBinding
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.signup.SignUpFragment
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhoneAuthFragment : Fragment() {
    private var email: String? = null
    private var password: String? = null

    private var _binding: FragmentPhoneAuthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PhoneAuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observePhoneAuth()
        observeAuthComplete()
    }

    private fun initView() {
        binding.btnSendCode.setOnClickListener {
            val num = binding.etPhoneNumber.text.toString()
            viewModel.requestPhoneAuth(num)
        }

        binding.btnVerifyCode.setOnClickListener {
            val code = binding.etCode.text.toString()
            if (code.isNotEmpty()) {
                viewModel.verifyCode(code)
            }
        }
    }

    private fun observePhoneAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.phoneAuthState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        // 폰번호인증성공
                        Log.d("PhoneAuth", "success")
                    }

                    is UiState.Error -> {
                        // 폰번호인증실패
                        Log.d("PhoneAuth", "failed")
                    }

                }
            }
        }
    }

    private fun observeAuthComplete() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.codeAuthState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        // 인증코드 성공
                        Log.d("Auth", "success")
                        parentFragmentManager
                            .beginTransaction()
                            .replace(R.id.frameLayout, HomeFragment())
                            .commit()
                    }

                    is UiState.Error -> {
                        // 인증코드 실패
                        Log.d("Auth", "failed")
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
