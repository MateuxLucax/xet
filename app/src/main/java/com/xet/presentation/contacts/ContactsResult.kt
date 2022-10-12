package com.xet.presentation.contacts

import com.xet.domain.model.Contact

data class ContactsResult(
    val success: List<Contact>? = null,
    val empty: Int? = null,
    val error: Int? = null
)