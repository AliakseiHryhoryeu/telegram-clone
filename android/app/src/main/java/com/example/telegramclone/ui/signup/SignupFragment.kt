package com.example.telegramclone.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.telegramclone.R
import com.example.telegramclone.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private var _binding: FragmentSignupBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel =
            ViewModelProvider(this).get(SignupViewModel::class.java)

        _binding = FragmentSignupBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.messengerLabel.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }
        binding.signupToLoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_login)
        }

        binding.googleBtn.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_home)
        }
        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_signup_to_home)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}