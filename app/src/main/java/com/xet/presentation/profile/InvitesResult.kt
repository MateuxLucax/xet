package com.xet.presentation.profile

import com.xet.domain.model.Contact

data class InvitesResult(
    val success: MutableList<Contact>? = null,
    val empty: Int? = null,
    val error: Int? = null
)