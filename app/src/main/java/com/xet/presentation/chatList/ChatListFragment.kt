package com.xet.presentation.chatList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.xet.databinding.FragmentChatListBinding
import com.xet.domain.model.Contact
import com.xet.domain.model.Status
import com.xet.presentation.chatList.components.ChatListAdapter

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

        if (container != null) {
            val recyclerView = binding.chatListRecyclerView
            recyclerView.adapter = ChatListAdapter(contacts(), container.context)
        }
        return binding.root
    }

    private fun contacts(): List<Contact> {

        return listOf(
            Contact(
                userId = "1",
                displayName = "Pedro Pinto",
                status = Status.ONLINE,
                lastMessage = "Good afternoon deadbeat"
            ),
            Contact(
                userId = "2",
                displayName = "Alfredo Alvares",
                status = Status.OFFLINE,
                lastMessage = "Hey! U owe me 100 bucks"
            ),
            Contact(
                userId = "3",
                displayName = "Uesley Ulisson",
                status = Status.UNDEFINED,
                lastMessage = "Im gonna beat U deadbeat"
            )
        )
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