package com.example.signup.presentation.signin

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentSignInBinding
import com.example.signup.presentation.addValidationTextWatcher
import com.example.signup.presentation.addValidationTextWatcher2
import com.example.signup.presentation.addValidationTextWatcher3
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.isValidEmail
import com.example.signup.presentation.isValidPassword
import com.example.signup.presentation.signup.SignUpFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SignInViewModel by viewModels()
    private val validMap = mutableMapOf<EditText, Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeSignIn()
        setUpTextWatcher()
    }

    private fun initView() {
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            viewModel.signIn(email, password)
        }
        binding.tvSignUp.setOnClickListener {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.frameLayout, SignUpFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.ivBackButton.setOnClickListener{
            parentFragmentManager.popBackStack()
        }

    }

    private fun setUpTextWatcher() {
        binding.etEmail.addValidationTextWatcher3(
            validMap,
            binding.btnSignIn
        )
        binding.etPassword.addValidationTextWatcher3(
            validMap,
            binding.btnSignIn
        )
    }

    private fun observeSignIn() {
        viewLifecycleOwner.lifecycleScope.launch {
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
                        Toast.makeText(requireActivity(), "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        parentFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_up,
                R.anim.slide_out_up,
                R.anim.slide_in_down,
                R.anim.slide_out_down
            )
            .replace(R.id.frameLayout, fragment)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}