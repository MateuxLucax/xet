package com.xet.domain.usecase.chat

data class ChatUseCases(
    val getMessages: GetMessagesUseCase,
    val sendMessageUseCase: SendMessageUseCase,
    val getFileUseCase: GetFileUseCase
)