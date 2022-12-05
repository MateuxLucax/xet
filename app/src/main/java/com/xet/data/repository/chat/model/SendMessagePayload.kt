package com.xet.data.repository.chat.model

import com.xet.domain.model.FileType

data class SendMessagePayload(
    val text: String? = null,
    val file: ByteArray? = null,
    val fileType: FileType? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SendMessagePayload

        if (text != other.text) return false
        if (file != null) {
            if (other.file == null) return false
            if (!file.contentEquals(other.file)) return false
        } else if (other.file != null) return false
        if (fileType != other.fileType) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text?.hashCode() ?: 0
        result = 31 * result + (file?.contentHashCode() ?: 0)
        result = 31 * result + (fileType?.hashCode() ?: 0)
        return result
    }
}