package com.example.telegramclone.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.telegramclone.databinding.FragmentLoginBinding
import com.example.telegramclone.R
import androidx.navigation.fragment.findNavController


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Buttons
        binding.loginToSignupLayout.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_signup)
        }

        binding.SubmitButton.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_home)
        }
        binding.googleBtn.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_home)
        }
        binding.loginToResetpasswordLayout.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_resetpassword)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


// val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }