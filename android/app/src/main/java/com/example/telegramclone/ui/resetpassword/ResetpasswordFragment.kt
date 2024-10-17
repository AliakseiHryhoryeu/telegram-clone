package com.example.telegramclone.ui.resetpassword

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.telegramclone.R
import com.example.telegramclone.databinding.FragmentResetpasswordBinding

class ResetpasswordFragment :Fragment() {
    private var _binding: FragmentResetpasswordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel =
            ViewModelProvider(this).get(ResetpasswordViewModel::class.java)

        _binding = FragmentResetpasswordBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.messengerLabel.setOnClickListener {
            findNavController().navigate(R.id.action_resetpassword_to_login)
        }
        binding.resetpasswordToLoginBtn.setOnClickListener{
            findNavController().navigate(R.id.action_resetpassword_to_login)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}