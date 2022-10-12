package com.xet.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xet.R
import com.xet.databinding.FragmentSearchBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.search.components.SearchAdapter

private const val USER_ID = "userId"

class SearchFragment(
    private var viewModel: SearchViewModel = ServiceLocator.getSearchViewModel()
) : Fragment() {
    private var userId: String? = null
    private lateinit var binding: FragmentSearchBinding

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
        binding = FragmentSearchBinding.inflate(layoutInflater)

        val loading = binding.searchListLoading
        val message = binding.searchMessage
        val searchInput = binding.searchContact
        val searchBtn = binding.contactSearchBtn

        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            loading.visibility = View.GONE
            searchBtn.isActivated = true
            if (result.error != null) {
                message.text = context?.getString(result.error)
                context?.getColor(R.color.errorColor)?.let { it1 -> message.setTextColor(it1) }
            } else if (result.empty != null) {
                message.text = context?.getString(result.empty)
            } else if (result.success != null && container != null) {
                val recyclerView = binding.searchContactRecyclerView
                recyclerView.adapter = SearchAdapter(result.success, container.context, viewModel::sendInvite, this::redirectToMessageActivity)
            }
        })

        viewModel.inviteResult.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.error != null) {
                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
            } else if (result.success != null) {
                Toast.makeText(context, result.success, Toast.LENGTH_SHORT).show()
            }
        })

        searchBtn.setOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotBlank()) {
                viewModel.search(query)
                searchBtn.isActivated = false
            }
        }

        userId?.let {
            loading.visibility = View.VISIBLE
            searchBtn.isActivated = false
            viewModel.setCurrentUserId(it)
            viewModel.search("")
        }

        return binding.root
    }

    private fun redirectToMessageActivity(userTo: String) {
        userId?.let {
            Toast.makeText(context, "Redirecting to chat between $userId and $userTo", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
}