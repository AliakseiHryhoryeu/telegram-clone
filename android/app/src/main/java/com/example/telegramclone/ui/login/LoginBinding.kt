package com.example.telegramclone.ui.login
import com.example.telegramclone.R

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView

class CustomLoginBinding(activity: AppCompatActivity) {

    // Инициализируем элементы вручную через findViewById
    val usernameOrEmailInput: EditText = activity.findViewById(R.id.login_email_or_username_input)
    val passwordInput: EditText = activity.findViewById(R.id.password_input)
    val loginButton: Button = activity.findViewById(R.id.submit_button)
    val forgotPasswordButton: TextView = activity.findViewById(R.id.login_forgot_password_btn)
    val loading : ProgressBar = activity.findViewById(R.id.loading)

    // Добавляем остальные элементы по необходимости
}