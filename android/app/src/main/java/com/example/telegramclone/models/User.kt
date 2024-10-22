package com.example.telegramclone.models


import java.time.LocalDateTime
import java.util.function.Predicate

class User {
    var id: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var description: String? = null
    var username: String = ""
    var email: String = ""
    var password: String = ""
    var profileImageUrl: String? = null
    var lastSeen: LocalDateTime? = null
    private var contacts: MutableList<Contact?>? = null
    private val blocked: MutableList<String>? = null

    fun getContacts(): List<Contact?>? {
        return contacts
    }

    fun updateContact(userId: String, messageId: String) {
        if (this.contacts == null) {
            this.contacts = ArrayList<Contact?>()
        }

        val existingContact: Contact? = contacts!!.stream()
            .filter(Predicate<Contact?> { contact -> contact.getUserId == userId })
            .findFirst()
            .orElse(null)

        if (existingContact != null) {
            existingContact.addMessageIdInContact(messageId)
        } else {
            val newContact: Contact = Contact()
            newContact.setUserId(userId)
            newContact.addMessageIdInContact(messageId)
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