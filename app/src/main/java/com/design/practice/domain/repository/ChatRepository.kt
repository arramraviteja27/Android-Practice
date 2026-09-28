package com.design.practice.domain.repository

import com.design.practice.domain.model.ChatMessage

interface ChatRepository {
    suspend fun sendMessage(
        message: String,
        previousMessages: List<ChatMessage>
    ): Result<String>
}