package com.xet.presentation.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xet.R
import com.xet.databinding.FragmentFriendsBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.friends.components.FriendsAdapter

private const val USER_ID = "user_id"

class ContactsFragment(
    private var viewModel: FriendsViewModel = ServiceLocator.getContactsViewModel()
) : Fragment() {
    private var userId: String? = null
    private lateinit var binding: FragmentFriendsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userId = it.getString(USER_ID)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFriendsBinding.inflate(layoutInflater)
        val loading = binding.friendListLoading
        val message = binding.friendListMessage

        viewModel.friendsResult.observe(viewLifecycleOwner, Observer {
            val friendsResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (friendsResult.error != null) {
                message.text = context?.getString(friendsResult.error)
                context?.getColor(R.color.errorColor)?.let { it1 -> message.setTextColor(it1) }
            } else if (friendsResult.empty != null) {
                message.text = context?.getString(friendsResult.empty)
            } else if (friendsResult.success != null && container != null) {
                val recyclerView = binding.friendListRecyclerView
                recyclerView.adapter = FriendsAdapter(friendsResult.success, container.context)
            }
        })

        userId?.let {
            loading.visibility = View.VISIBLE
            viewModel.getContacts(it)
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ContactsFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
}