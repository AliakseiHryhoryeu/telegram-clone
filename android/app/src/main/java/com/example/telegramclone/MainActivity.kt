package com.example.telegramclone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

    }
}

//        setContentView(R.layout.activity_main)
//        setContentView(R.layout.signup)
//        setContentView(R.layout.login)
//       setContentView(R.layout.resetpassword)
//       setContentView(R.layout.page404)

