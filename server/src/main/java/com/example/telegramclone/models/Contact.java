package com.example.telegramclone.models;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "contacts")
public class Contact {
	private String userId;
	private List<String> messages; // Array of message IDs

	public Contact() {
		this.messages = new ArrayList<>(); // Initialize the messages list
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void addMessageIdInContact(String messageId) {
		this.messages.add(messageId); // Add the message ID to the list of messages id
	}

	public List<String> getMessagesIdFromContact() {
		return messages; // Return the list of message IDs
	}
}
