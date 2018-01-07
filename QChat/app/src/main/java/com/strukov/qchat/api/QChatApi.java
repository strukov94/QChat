package com.strukov.qchat.api;

import com.strukov.qchat.models.Friend;
import com.strukov.qchat.models.Message;
import com.strukov.qchat.models.Messages;
import com.strukov.qchat.models.User;
import com.strukov.qchat.models.Users;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Matthew on 23.12.2017.
 */

public interface QChatApi {
    @GET("users/all")
    Observable<Users> getUsers();

    @GET("users/{user}")
    Observable<User> getUser(@Path("user") String user);

    @Headers("Content-Type: application/json")
    @POST("user/new")
    Observable<User> newUser(@Body User user);

    @Headers("Content-Type: application/json")
    @PUT("user/update")
    Observable<User> updateUser(@Body User user);

    @Headers("Content-Type: application/json")
    @POST("users/{user}/message/new")
    Observable<Message> newMessage(@Path("user") String user, @Body Message message);

    @GET("users/{user}/messages/all")
    Observable<Messages> getMessages(@Path("user") int user, @Query("is_sent") boolean isSent);

    @Headers("Content-Type: application/json")
    @POST("users/{user}/friends/new")
    Observable<Friend> newFriend(@Path("user") String user, @Body Friend friend);

    @GET("users/{user}/friends/all")
    Observable<Friend> getFriends(@Path("user") String user);
}
