package com.example.signup.presentation.phoneAuth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentPhoneAuthBinding
import com.example.signup.presentation.addValidationTextWatcher
import com.example.signup.presentation.addValidationTextWatcher2
import com.example.signup.presentation.addValidationTextWatcher4
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.isValidEmail
import com.example.signup.presentation.isValidPassword
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
    private val validMap = mutableMapOf<EditText, Boolean>()

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
        setUpTextWatcher()
    }

    private fun initView() {
        binding.btnSendCode.setOnClickListener {
            val num = binding.etPhoneNumber.text.toString()
            viewModel.requestPhoneAuth("+$num")
        }

        binding.btnVerifyCode.setOnClickListener {
            val code = binding.etCode.text.toString()
            if (code.isNotEmpty()) {
                viewModel.verifyCode(code)
            }
        }

        binding.ivBackButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }
    }


    private fun setUpTextWatcher() {
        binding.etPhoneNumber.addValidationTextWatcher4(
            11,
            getString(R.string.num_size_eleven),
            validMap,
            binding.btnSendCode,
            binding.tvPhoneRegex
        )
        binding.etCode.addValidationTextWatcher4(
            6,
            getString(R.string.num_size_six),
            validMap,
            binding.btnVerifyCode,
            binding.tvCodeRegex
        )
    }

    private fun observePhoneAuth() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.phoneAuthState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }

                    is UiState.Success -> {
                        // 폰번호인증성공
                        Toast.makeText(requireActivity(), "인증번호를 확인해주세요.", Toast.LENGTH_SHORT).show()
                        binding.etPhoneNumber.isEnabled = false
                        binding.btnSendCode.isEnabled = false
                        binding.btnSendCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_enable_radius)
                        binding.etCode.isEnabled = true
                        binding.etCode.setText("")
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
                        Toast.makeText(requireActivity(), "회원가입 성공", Toast.LENGTH_SHORT).show()
                    }

                    is UiState.Error -> {
                        binding.etPhoneNumber.isEnabled = true
                        binding.etCode.isEnabled = false
                        binding.btnSendCode.text = "재인증."
                        binding.btnSendCode.isEnabled = true
                        binding.btnSendCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_primary_radius)
                        binding.btnVerifyCode.isEnabled = false
                        binding.btnVerifyCode.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_enable_radius)


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
