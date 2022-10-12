package com.xet.presentation.search

import com.xet.domain.model.Contact

data class SearchResult(
    val success: List<Contact>? = null,
    val empty: Int? = null,
    val error: Int? = null,
    val clear: Boolean = false
)
