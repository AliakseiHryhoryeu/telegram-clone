package com.example.telegramclone.models


class Contact() {
    private var userId: String = ""
    private val messages: MutableList<String> = ArrayList()

    fun setUserId(newUserId:String){
        userId = newUserId
    }

    val getUserId: String
        get()= userId
    
    fun addMessageIdInContact(messageId: String) {
        messages.add(messageId)
    }

    val messagesIdFromContact: List<String>
        get() = messages
}
