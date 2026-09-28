package com.design.practice.domain.model

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isFromUser: Boolean
)
