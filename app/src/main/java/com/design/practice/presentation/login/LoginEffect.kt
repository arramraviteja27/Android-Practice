package com.design.practice.presentation.login

sealed interface LoginEffect {

    data object NavigateToHome : LoginEffect

    data object NavigateToSignup : LoginEffect

    data class ShowSnackbar(val message: String) : LoginEffect
}