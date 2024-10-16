package com.example.telegramclone.ui.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val usernameOrEmailError: Int? = null,
    val passwordError: Int? = null,
    val isDataValid: Boolean = false
)