package com.xet.domain.model

import com.xet.R

enum class Status {
    ONLINE, OFFLINE;

    fun toResourceString(): Int {
         return when(this) {
             ONLINE  -> R.string.status_online
             OFFLINE -> R.string.status_offline
         }
    }

    fun toColor(): Int {
        return when(this) {
            ONLINE  -> R.color.successColor
            OFFLINE -> R.color.errorColor
        }
    }

}