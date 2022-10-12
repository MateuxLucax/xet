package com.xet.presentation.contacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xet.R
import com.xet.data.Result
import com.xet.domain.usecase.contact.ContactUseCases
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val useCases: ContactUseCases,
): ViewModel() {

    private val _contactsResult = MutableLiveData<ContactsResult>()
    val contactsResult: LiveData<ContactsResult> = _contactsResult

    fun getContacts(userId: String) {
        viewModelScope.launch {
            val result = useCases.getContacts(userId)

            if (result is Result.Success) {
                if (result.data.isEmpty()) {
                    _contactsResult.value = ContactsResult(empty = R.string.contacts_list_empty)
                } else {
                    _contactsResult.value = ContactsResult(success = result.data)
                }
            } else {
                _contactsResult.value = ContactsResult(error = R.string.contacts_list_fail)
            }
        }
    }

}