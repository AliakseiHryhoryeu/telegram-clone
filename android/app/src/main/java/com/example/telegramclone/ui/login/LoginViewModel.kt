package com.example.telegramclone.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.example.telegramclone.data.LoginRepository
import com.example.telegramclone.data.Result

import com.example.telegramclone.R

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(usernameOrEmail: String, password: String) {
        // can be launched in a separate asynchronous job
        val result = loginRepository.login(usernameOrEmail, password)

        if (result is Result.Success) {
            _loginResult.value =
                LoginResult(success = LoggedInUserView(displayName = result.data.displayName))
        } else {
            _loginResult.value = LoginResult(error = R.string.login_failed)
        }
    }

    fun loginDataChanged(usernameOrEmail: String, password: String) {
        if (!isUserNameValid(usernameOrEmail)) {
            _loginForm.value = LoginFormState(usernameOrEmailError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(usernameOrEmail: String): Boolean {
        return if (usernameOrEmail.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(usernameOrEmail).matches()
        } else {
            usernameOrEmail.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}