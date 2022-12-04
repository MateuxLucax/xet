package com.xet.domain.model

data class Message(
    val id: String,
    val text: String?,
    val file: ByteArray?,
    val sentAt: String,
    val isMine: Boolean
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (id != other.id) return false
        if (text != other.text) return false
        if (file != null) {
            if (other.file == null) return false
            if (!file.contentEquals(other.file)) return false
        } else if (other.file != null) return false
        if (sentAt != other.sentAt) return false
        if (isMine != other.isMine) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + (file?.contentHashCode() ?: 0)
        result = 31 * result + sentAt.hashCode()
        result = 31 * result + isMine.hashCode()
        return result
    }
}