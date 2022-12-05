package com.xet.domain.model

enum class FileType {
    AUDIO;

    fun toExtension(): String {
        return when(this) {
            AUDIO  -> "3gp"
        }
    }

    companion object {
        fun fromExtension(extension: String): FileType {
            return when(extension) {
                "3gp" -> AUDIO
                else -> AUDIO
            }
        }
    }

}