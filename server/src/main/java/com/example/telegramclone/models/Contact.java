package com.example.telegramclone.models;

import lombok.Data;
import java.util.List;

@Data
public class Contact {
	private String contactId; // ID другого пользователя
	private List<String> messageIds; // Список сообщений с этим пользователем
}