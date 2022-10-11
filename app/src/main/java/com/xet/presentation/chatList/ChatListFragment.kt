package com.xet.presentation.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xet.databinding.FragmentChatListBinding

private const val USER = "user"

class ChatListFragment : Fragment() {
    private var user: String? = null
    private lateinit var binding: FragmentChatListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getString(USER)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatListBinding.inflate(layoutInflater)

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ChatListFragment().apply {
                arguments = Bundle().apply {
                    putString(USER, param1)
                }
            }
    }
}