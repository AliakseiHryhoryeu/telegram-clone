package com.example.telegramclone.ui.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.telegramclone.R
import com.example.telegramclone.databinding.FragmentMessagesBinding
import com.example.telegramclone.ui.messages.MessagesViewModel

class MessagesFragment : Fragment() {
    private var _binding: FragmentMessagesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val messagesViewModel =
            ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //   Buttons
        binding.ContactInfoBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messages_to_contact)
        }
        binding.contactDrawMenuBtn1.setOnClickListener {
            findNavController().navigate(R.id.action_messages_to_contact)
        }
        binding.ContactBackBtn.setOnClickListener {
            findNavController().navigate(R.id.action_messages_to_nav_home)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}