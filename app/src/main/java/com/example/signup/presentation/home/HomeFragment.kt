package com.example.signup.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentHomeBinding
import com.example.signup.presentation.signin.SignInFragment
import com.example.signup.presentation.signin.SignInViewModel
import com.example.signup.presentation.splash.SplashFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSignOut()
        observeWithdrawal()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initView() {
        binding.tvSignOut.setOnClickListener {
            viewModel.signOut()
        }

        binding.tvWithdrawal.setOnClickListener {
            viewModel.withdraw()
        }
    }



    private fun observeSignOut() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signOutState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        replaceFragment(SplashFragment())
                    }
                    is UiState.Error -> {

                    }
                }
            }
        }
    }

    private fun observeWithdrawal() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.withdrawalState.collect { state ->
                when (state) {
                    is UiState.Loading -> {

                    }
                    is UiState.Success -> {
                        replaceFragment(SplashFragment())
                    }
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
}
