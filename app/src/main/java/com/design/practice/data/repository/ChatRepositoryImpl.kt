package com.design.practice.data.repository

import com.design.practice.domain.model.ChatMessage
import com.design.practice.domain.repository.ChatRepository
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend
import jakarta.inject.Inject

class ChatRepositoryImpl @Inject constructor() : ChatRepository {

    private val model = Firebase.ai(
        backend = GenerativeBackend.googleAI()
    ).generativeModel(
        modelName = "gemini-3.6-flash"
    )

    override suspend fun sendMessage(
        message: String,
        previousMessages: List<ChatMessage>
    ): Result<String> {

        return runCatching {

            val conversationHistory = buildString {

                previousMessages.forEach { chat ->

                    val sender = if (chat.isFromUser) {
                        "User"
                    } else {
                        "Assistant"
                    }

                    append("$sender: ${chat.text}\n")
                }

                append("User: $message\n")
                append("Assistant:")
            }

            val response = model.generateContent(
                conversationHistory
            )

            response.text
                ?: "Sorry, I couldn't generate a response."
        }
    }
}