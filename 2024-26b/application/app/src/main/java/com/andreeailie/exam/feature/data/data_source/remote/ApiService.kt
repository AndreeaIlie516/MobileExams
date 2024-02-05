package com.andreeailie.exam.feature.data.data_source.remote

import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.EntityServer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/events")
    suspend fun getEvents(): Response<List<Entity>>

    @GET("/event/{id}")
    suspend fun getEventById(@Path("id") id: Int): Response<Unit>

    @POST("/event")
    suspend fun addEvent(@Body entity: EntityServer): Response<Unit>


    @DELETE("/event/{id}")
    suspend fun deleteEvent(@Path("id") id: Int): Response<Unit>

    @PUT("/activity/{id}")
    suspend fun updateEvent(@Path("id") id: Int, @Body entity: EntityServer): Response<Unit>

    @GET("/inProgress")
    suspend fun getEventsInProgress(): Response<List<Entity>>

    @GET("/allEvents")
    suspend fun getAllEvents(): Response<List<Entity>>

    @PUT("/enroll/{id}")
    suspend fun enrollInEvent(@Path("id") id: Int): Response<Unit>
}