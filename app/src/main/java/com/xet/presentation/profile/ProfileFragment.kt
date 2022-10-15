package com.xet.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.textfield.TextInputEditText
import com.xet.R
import com.xet.databinding.FragmentProfileBinding
import com.xet.presentation.ServiceLocator
import com.xet.presentation.login.afterTextChanged
import com.xet.presentation.profile.components.InvitesAdapter

private const val USER_ID = "userId"

class ProfileFragment(
    private val viewModel: ProfileViewModel = ServiceLocator.getProfileViewModel()
) : Fragment() {
    private var userId: String? = null
    private lateinit var binding: FragmentProfileBinding

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
        binding = FragmentProfileBinding.inflate(layoutInflater)

        val loading = binding.profileInvitesLoading
        val invitesMessage = binding.profileInvitesMessage
        val updateBtn = binding.profileUpdateBtn
        val fullName = binding.profileNameInput
        val username = binding.profileUsernameInput

        viewModel.updateProfile.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            updateBtn.text = context?.getText(R.string.profile_update_btn)
            updateBtn.isEnabled = true
            if (result) {
                Toast.makeText(context, context?.getText(R.string.profile_update_success) ?: "", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, context?.getText(R.string.profile_update_fail) ?: "", Toast.LENGTH_SHORT).show()
            }
        })

        updateBtn.setOnClickListener {
            updateBtn.text = context?.getText(R.string.updating)
            updateBtn.isEnabled = false
            viewModel.updateProfile(fullName.text.toString(), username.text.toString())
        }

        viewModel.invites.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            loading.visibility = View.GONE
            if (result.empty != null) {
                invitesMessage.text = context?.getString(result.empty)
            } else if (result.error != null) {
                invitesMessage.text = context?.getString(result.error)
                context?.getColor(R.color.errorColor)?.let { it1 -> invitesMessage.setTextColor(it1) }
            } else if (result.success != null && container != null) {
                val recyclerView = binding.profileInvites
                recyclerView.adapter = InvitesAdapter(result.success, container.context, viewModel::updateInvite)
            }
        })

        viewModel.updateInvite.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            if (result.success != null) {
                Toast.makeText(context, context?.getText(result.success) ?: "", Toast.LENGTH_SHORT).show()
            } else if (result.error != null) {
                Toast.makeText(context, context?.getText(result.error) ?: "", Toast.LENGTH_SHORT).show()
            }
        })

        viewModel.profileFormState.observe(viewLifecycleOwner, Observer {
            val loginState = it ?: return@Observer

            updateBtn.isEnabled = loginState.isDataValid
            if (loginState.fullNameError != null) {
                fullName.error = getString(loginState.fullNameError)
            }
            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
        })

        loadUser(fullName, username)
        loading.visibility = View.VISIBLE
        viewModel.loadInvites()

        return binding.root
    }

    private fun loadUser(fullNameInput: TextInputEditText, usernameInput: TextInputEditText) {
        val result = viewModel.loadUser()

        if (result.success != null) {
            val user = result.success

            fullNameInput.setText(user.displayName)
            usernameInput.setText(user.username)

            fullNameInput.afterTextChanged {
                viewModel.formChanged(
                    fullNameInput.text.toString(),
                    usernameInput.text.toString()
                )
            }
            usernameInput.afterTextChanged {
                viewModel.formChanged(
                    fullNameInput.text.toString(),
                    usernameInput.text.toString()
                )
            }
        } else if (result.error != null) {
            Toast.makeText(context, result.error, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, param1)
                }
            }
    }
}