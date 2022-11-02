package com.xet.presentation.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.xet.R
import com.xet.databinding.FragmentSearchBinding
import com.xet.domain.model.Contact
import com.xet.presentation.ServiceLocator
import com.xet.presentation.search.components.SearchAdapter

private const val USER_TOKEN = "userToken"

class SearchFragment(
    private var viewModel: SearchViewModel = ServiceLocator.getSearchViewModel()
) : Fragment() {
    private var userToken: String? = null
    private lateinit var binding: FragmentSearchBinding
    private val contacts: MutableList<Contact> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userToken = it.getString(USER_TOKEN)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater)

        val loading = binding.searchListLoading
        val message = binding.searchMessage
        val searchInput = binding.searchContact
        val recyclerView = binding.searchContactRecyclerView

        val adapter = container?.let {
            val friendCallback = this::redirectToMessageActivity
            val sendInviteCallback = viewModel::sendInvite
            SearchAdapter(contacts, it.context, friendCallback, sendInviteCallback)

            // TODO when the user send invite, the other user should be updated
            //  - change icon
            //  - change btn listener (in this case, remove)
        }

        recyclerView.adapter = adapter


        viewModel.searchResult.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer
            loading.visibility = View.GONE
            if (result.error != null) {
                message.text = context?.getString(result.error)
                context?.getColor(R.color.errorColor)?.let { it1 -> message.setTextColor(it1) }
            } else if (result.empty != null) {
                message.text = context?.getString(result.empty)
            } else if (result.success != null) {
                contacts.addAll(result.success)
                adapter?.notifyDataSetChanged()
            }
        })

        viewModel.updateInviteResult.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.error != null) {
                Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
            } else if (result.success != null) {
                Toast.makeText(context, result.success, Toast.LENGTH_SHORT).show()
            }
        })

        searchInput.afterTextChangedDelayed {
            val query = searchInput.text.toString()
            loading.visibility = View.VISIBLE
            contacts.clear()
            adapter?.notifyDataSetChanged()
            viewModel.search(query)
        }

        userToken?.let {
            loading.visibility = View.VISIBLE
            viewModel.setCurrentUserToken(it)
            viewModel.search("")
        }

        return binding.root
    }

    private fun redirectToMessageActivity(userTo: String) {
        userToken?.let {
            Toast.makeText(context, "Redirecting to chat between $userToken and $userTo", Toast.LENGTH_SHORT).show()
            // TODO actually redirect (?)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_TOKEN, param1)
                }
            }
    }
}

fun TextView.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        var delay = 1000
        var countDown = 1500

        var timer: CountDownTimer? = null

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            timer?.cancel()
            timer = object : CountDownTimer(delay.toLong(), countDown.toLong()) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    afterTextChanged.invoke(editable.toString())
                }
            }.start()
        }
    })
}
