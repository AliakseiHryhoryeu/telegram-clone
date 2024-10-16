package com.example.telegramclone

import android.os.Build
import android.os.Bundle
import android.view.WindowInsetsController
import android.graphics.Color
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // DARK THEME for Android
        enableEdgeToEdge() // Border less

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            // For version Android 11 or lower
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = 0
        }
    }
}


//        setContentView(R.layout.activity_main)
//        setContentView(R.layout.signup)
//        setContentView(R.layout.login)
//       setContentView(R.layout.resetpassword)
//       setContentView(R.layout.page404)

