package com.example.telegramclone.models

import java.time.LocalDateTime

class Message {

    var id: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var description: String? = null
    var username: String? = null
    var email: String? = null
    var password: String? = null
    var profileImageUrl: String? = null
    var lastSeen: LocalDateTime? = null
    private var contacts: MutableList<Contact?>? = null
    private val blocked: MutableList<String>? = null

    fun getContacts(): List<Contact?>? {
        return contacts
    }

    fun updateContact(userId: String, messageId: String?) {
        if (this.contacts == null) {
            this.contacts = ArrayList()
        }

        val existingContact = contacts!!.stream()
            .filter { c: Contact? -> c!!.userId == userId }
            .findFirst()
            .orElse(null)

        if (existingContact != null) {
            existingContact.addMessageIdInContact(messageId!!)
        } else {
            val newContact = Contact()
            newContact.userId = userId
            newContact.addMessageIdInContact(messageId!!)
            contacts!!.add(newContact)
        }
    }

    fun addBlocked(id: String) {
        blocked!!.add(id)
    }

    fun removeBlocked(id: String) {
        blocked!!.remove(id)
    }

    fun getBlocked(): List<String>? {
        return blocked
    }
}
