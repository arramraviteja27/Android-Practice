package com.design.practice.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {

    var email by remember { mutableStateOf("") }

    var pswrd by remember { mutableStateOf("") }


}