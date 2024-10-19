package com.example.telegramclone.DTO.Message;

public class DeleteAllMessagesByContactIdDTO {
	private String contactId;

	public DeleteAllMessagesByContactIdDTO() {
	}

	public DeleteAllMessagesByContactIdDTO(String contactid) {
		this.contactId = contactid;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
}
