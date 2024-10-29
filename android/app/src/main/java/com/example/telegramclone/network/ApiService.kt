package com.example.telegramclone.network

import com.example.telegramclone.dto.CreateUserResponse
import com.example.telegramclone.models.Message
import com.example.telegramclone.models.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    //    Auth
    // Продление jwt токена
    @POST("newJwt")
    fun newJwt(
        @Header("Authorization") token: String
    ): Call<String>

    @POST("login")
    fun login(
        @Query("login") login: String,
        @Query("password") password: String
    ): Call<String>

    //    User

    @POST("createUser")
    fun createUser(
        @Query("login") login: String,
        @Query("username") username: String,
        @Query("email") email: String,
        @Query("password") password: String
    ): Call<CreateUserResponse>
//    <user> <jwt>

    @POST("changeUsername")
    fun changeUsername(
        @Header("Authorization") token: String,
        @Query("newusername") newusername: String,
    ): Call<User>

    @POST("changePassword")
    fun changePassword(
        @Header("Authorization") token: String,
        @Query("oldPassword") oldPassword: String,
        @Query("newPassword") newPassword: String,
    ): Call<User>

    @POST("changeDescription")
    fun changeDescription(
        @Header("Authorization") token: String,
        @Query("newDescription") newDescription: String,
    ): Call<User>

    @POST("changeFirstname")
    fun changeFirstname(
        @Header("Authorization") token: String,
        @Query("firstname") firstname: String,
    ): Call<User>

    @POST("changeLastname")
    fun changeLastname(
        @Header("Authorization") token: String,
        @Query("lastname") lastname: String,
    ): Call<User>

    @POST("changeLastSeen")
    fun changeLastSeen(
        @Header("Authorization") token: String
    ): Call<User>

    @POST("searchUsers")
    fun searchUsers(
        @Header("Authorization") token: String,
        @Query("searchTerm") userId: String,
    ): Call<User>

    @POST("blockUser")
    fun blockUser(
        @Header("Authorization") token: String,
        @Query("userId") userId: String,
    ): Call<User>


    //    Messages

    @POST("createMessage")
    fun createMessage(
        @Header("Authorization") token: String,
        @Query("toUserId") toUserId: String,
        @Query("message") message: String
    ): Call<Message>

    @POST("readMessage")
    fun readMessage(
        @Header("Authorization") token: String,
        @Query("messageId") messageId: String
    ): Call<Message>

    @POST("getMessagesByContactId")
    fun getMessagesByContactId(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Call<List<Message>>

    @POST("getMessage")
    fun getMessage(
        @Header("Authorization") token: String,
        @Query("messageId") messageId: String
    ): Call<Message>

    @POST("updateMessage")
    fun updateMessage(
        @Header("Authorization") token: String,
        @Query("messageId") messageId: String,
        @Query("message") message: String
    ): Call<Message>


    @POST("deleteMessage")
    fun deleteMessage(
        @Header("Authorization") token: String,
        @Query("messageId") messageId: String
    )

    @POST("deleteAllMessagesByContactId")
    fun deleteAllMessagesByContactId(
        @Header("Authorization") token: String,
        @Query("contactId") contactId: String
    )
}