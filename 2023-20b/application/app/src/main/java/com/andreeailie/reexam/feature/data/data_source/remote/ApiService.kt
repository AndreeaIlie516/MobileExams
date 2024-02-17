package com.andreeailie.reexam.feature.data.data_source.remote

import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.EntityServer
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/all")
    suspend fun getAllEntities(): Response<List<Entity>>

    @GET("/meal/{id}")
    suspend fun getEntityById(@Path("id") id: Int): Response<Entity>

    @GET("/types")
    suspend fun getAllProperties(): Response<List<String>>

    @GET("/meals/{type}")
    suspend fun getEntitiesByProperty(@Path("type") type: String): Response<List<Entity>>

    @POST("/meal")
    suspend fun addEntity(@Body entity: EntityServer): Response<Unit>

    @DELETE("/meal/{id}")
    suspend fun deleteEntity(@Path("id") id: Int): Response<Unit>

    @PUT("/activity/{id}")
    suspend fun updateEntity(@Path("id") id: Int, @Body entity: EntityServer): Response<Unit>
}