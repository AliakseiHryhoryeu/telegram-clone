package com.example.telegramclone.repository

import com.example.telegramclone.models.Message
import com.example.telegramclone.network.RetrofitClient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MessageRepository {

    private val apiService = RetrofitClient.apiService

    fun getMessage(token: String, messageId: String): LiveData<Message> {
        val messagesLiveData = MutableLiveData<Message>()

        apiService.getMessage("Bearer $token", messageId).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    messagesLiveData.value = response.body()
                } else {
                    Log.e("MessageRepository", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.e("MessageRepository", "Failed to fetch messages", t)
            }
        })

        return messagesLiveData
    }


    fun getMessagesByContactId(token: String, messageId: String): LiveData<Message> {
        val messagesLiveData = MutableLiveData<Message>()

        apiService.getMessage("Bearer $token", messageId).enqueue(object : Callback<Message> {
            override fun onResponse(call: Call<Message>, response: Response<Message>) {
                if (response.isSuccessful) {
                    messagesLiveData.value = response.body()
                } else {
                    Log.e("MessageRepository", "Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Message>, t: Throwable) {
                Log.e("MessageRepository", "Failed to fetch messages", t)
            }
        })

        return messagesLiveData
    }
}
