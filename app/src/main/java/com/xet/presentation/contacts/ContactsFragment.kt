package com.xet.presentation.contacts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xet.R
import com.xet.databinding.FragmentContactsBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.contacts.components.ContactsAdapter

private const val USER_ID = "user_id"

class ContactsFragment(
    private var viewModel: ContactsViewModel = ServiceLocator.getContactsViewModel()
) : Fragment() {
    private var userId: String? = null
    private lateinit var binding: FragmentContactsBinding

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
        binding = FragmentContactsBinding.inflate(layoutInflater)
        val loading = binding.contactListLoading
        val message = binding.contactListMessage

        viewModel.contactsResult.observe(viewLifecycleOwner, Observer {
            val loginResult = it ?: return@Observer

            loading.visibility = View.GONE
            if (loginResult.error != null) {
                message.text = context?.getString(loginResult.error)
                context?.getColor(R.color.errorColor)?.let { it1 -> message.setTextColor(it1) }
            } else if (loginResult.empty != null) {
                message.text = context?.getString(loginResult.empty)
            } else if (loginResult.success != null && container != null) {
                val recyclerView = binding.contactListRecyclerView
                recyclerView.adapter = ContactsAdapter(loginResult.success, container.context)
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