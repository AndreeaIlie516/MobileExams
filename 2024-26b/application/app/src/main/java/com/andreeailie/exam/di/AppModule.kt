package com.andreeailie.exam.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.andreeailie.exam.feature.data.data_source.local.EntityDatabase
import com.andreeailie.exam.feature.data.data_source.remote.ApiService
import com.andreeailie.exam.feature.data.network.NetworkChecker
import com.andreeailie.exam.feature.data.network.WebSocketService
import com.andreeailie.exam.feature.data.repository.LocalEntityRepositoryImpl
import com.andreeailie.exam.feature.data.repository.RemoteEntityRepositoryImpl
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.exam.feature.domain.repository.RemoteEntityRepository
import com.andreeailie.exam.feature.domain.use_case.AddEntityUseCase
import com.andreeailie.exam.feature.domain.use_case.DeleteEntityUseCase
import com.andreeailie.exam.feature.domain.use_case.EnrollInEventUseCase
import com.andreeailie.exam.feature.domain.use_case.EntityUseCases
import com.andreeailie.exam.feature.domain.use_case.GetAllEntitiesUseCase
import com.andreeailie.exam.feature.domain.use_case.GetEntitiesByPropertyUseCase
import com.andreeailie.exam.feature.domain.use_case.GetEntityUseCase
import com.andreeailie.exam.feature.domain.use_case.GetInProgressEventsUseCase
import com.andreeailie.exam.feature.domain.use_case.GetPropertiesUseCase
import com.andreeailie.exam.feature.domain.use_case.UpdateEntityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun provideEntityDatabase(app: Application): EntityDatabase {
        return Room.databaseBuilder(
            app,
            EntityDatabase::class.java,
            EntityDatabase.DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideNetworkChecker(@ApplicationContext context: Context): NetworkChecker {
        return NetworkChecker(context)
    }


    @Singleton
    @Provides
    fun provideWebSocketService(networkChecker: NetworkChecker): WebSocketService {
        val webSocketUrl = "ws://10.0.2.2:2426/ws"
        return WebSocketService(networkChecker, webSocketUrl)
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2426")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun provideLocalEntityRepository(
        db: EntityDatabase,
    ): LocalEntityRepository {
        return LocalEntityRepositoryImpl(db.entityDao)
    }

    @Provides
    fun provideRemoteEventRepository(
        apiService: ApiService,
        webSocketService: WebSocketService
    ): RemoteEntityRepository {
        return RemoteEntityRepositoryImpl(apiService, webSocketService)
    }

    @Provides
    @Singleton
    fun provideEntityUseCases(
        localRepository: LocalEntityRepository,
        remoteRepository: RemoteEntityRepository,
        networkChecker: NetworkChecker
    ): EntityUseCases {
        return EntityUseCases(
//            getPropertiesUseCase = GetPropertiesUseCase(
//                localRepository = localRepository,
//                remoteRepository = remoteRepository,
//                networkChecker = networkChecker
//            ),
            getAllEntitiesUseCase = GetAllEntitiesUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
            ),
//            getEntitiesByPropertyUseCase = GetEntitiesByPropertyUseCase(
//                localRepository = localRepository,
//            ),
            getEntityUseCase = GetEntityUseCase(localRepository),
            addEntityUseCase = AddEntityUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
                ),
            deleteEntityUseCase = DeleteEntityUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
            ),
            updateEntityUseCase = UpdateEntityUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
            ),
            enrollInEventUseCase = EnrollInEventUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
            ),
            getInProgressEventsUseCase = GetInProgressEventsUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkChecker = networkChecker
            )
        )
    }
}