package com.example.signup.presentation.splash

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.signup.R
import com.example.signup.UiState
import com.example.signup.databinding.FragmentSplashBinding
import com.example.signup.presentation.home.HomeFragment
import com.example.signup.presentation.phoneAuth.PhoneAuthFragment
import com.example.signup.presentation.signin.SignInFragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        observeSignIn()
        observeCurrentUser()

    }

    private fun initView() {
        viewModel.getCurrentUser()

        binding.btnGoogleSignIn.setOnClickListener {
            startLoginGoogle()
        }

        binding.tvEmailLogin.setOnClickListener {
            replaceFragment(SignInFragment(), true)
        }

        binding.tvPhoneLogin.setOnClickListener {
            replaceFragment(PhoneAuthFragment(), true)
        }
    }

    private fun observeSignIn() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signInState.collect { state ->
                when (state) {
                    is UiState.Loading -> {
                        //No action needed
                    }
                    is UiState.Success -> {
                        replaceFragment(HomeFragment(), false)
                    }
                    is UiState.Error -> {

                    }
                }
            }
        }
    }

    private fun observeCurrentUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.currentUserState.collect { state ->
                    when (state) {
                        is UiState.Loading -> {

                        }

                        is UiState.Success ->{
                            Toast.makeText(requireContext(), "구글 로그인 성공", Toast.LENGTH_SHORT).show()
                            replaceFragment(HomeFragment(), false)
                        }
                        is UiState.Error -> {

                        }
                    }
                }
            }
        }
    }

    private fun startLoginGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        googleLoginResult.launch(googleSignInClient!!.signInIntent)
    }

    private val googleLoginResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if (task.isSuccessful) {
                val idToken = task.result.idToken
                if (idToken != null) {
                    viewModel.signInWithGoogle(idToken)
                }
            } else {
                Toast.makeText(requireContext(), "구글 로그인 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment, add: Boolean) {
        if (add) {
            parentFragmentManager
                .beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
                .replace(R.id.frameLayout, fragment)
                .addToBackStack(null)
                .commit()
        } else {
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}