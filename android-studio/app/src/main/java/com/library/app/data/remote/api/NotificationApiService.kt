package com.library.app.data.remote.api

import com.library.app.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface NotificationApiService {

    @POST("api/notifications")
    suspend fun createNotification(@Body notification: NotificationCreateDTO): Response<NotificationResponseDTO>

    @GET("api/notifications/user/{userId}")
    suspend fun getUserNotifications(
        @Path("userId") userId: Long,
        @Query("unreadOnly") unreadOnly: Boolean? = null
    ): Response<List<NotificationResponseDTO>>

    @PATCH("api/notifications/{notificationId}/read")
    suspend fun markAsRead(@Path("notificationId") notificationId: Long): Response<NotificationResponseDTO>

    @PATCH("api/notifications/user/{userId}/read-all")
    suspend fun markAllAsRead(@Path("userId") userId: Long): Response<Unit>

    @DELETE("api/notifications/{notificationId}")
    suspend fun deleteNotification(@Path("notificationId") notificationId: Long): Response<Unit>

    @GET("api/notifications/user/{userId}/unread-count")
    suspend fun getUnreadCount(@Path("userId") userId: Long): Response<UnreadCountResponseDTO>
}


