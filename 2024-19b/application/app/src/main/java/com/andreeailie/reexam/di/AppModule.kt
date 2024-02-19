package com.andreeailie.reexam.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.andreeailie.reexam.feature.data.data_source.local.EntityDatabase
import com.andreeailie.reexam.feature.data.data_source.remote.ApiService
import com.andreeailie.reexam.feature.data.network.NetworkStatusTracker
import com.andreeailie.reexam.feature.data.network.WebSocketClient
import com.andreeailie.reexam.feature.data.repository.LocalEntityRepositoryImpl
import com.andreeailie.reexam.feature.data.repository.RemoteEntityRepositoryImpl
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import com.andreeailie.reexam.feature.domain.repository.RemoteEntityRepository
import com.andreeailie.reexam.feature.domain.use_case.AddEntityUseCase
import com.andreeailie.reexam.feature.domain.use_case.DeleteEntityUseCase
import com.andreeailie.reexam.feature.domain.use_case.EntityUseCases
import com.andreeailie.reexam.feature.domain.use_case.GetAllEntitiesUseCase
import com.andreeailie.reexam.feature.domain.use_case.GetAllPropertiesUseCase
import com.andreeailie.reexam.feature.domain.use_case.GetEntitiesByPropertyUseCase
import com.andreeailie.reexam.feature.domain.use_case.GetEntityUseCase
import com.andreeailie.reexam.feature.domain.use_case.UpdateEntityUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
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
    fun provideNetworkStatusTracker(@ApplicationContext context: Context): NetworkStatusTracker {
        return NetworkStatusTracker(context)
    }


    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:2419")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideWebSocketClient(okHttpClient: OkHttpClient): WebSocketClient {
        return WebSocketClient(okHttpClient)
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
        //webSocketService: WebSocketService
    ): RemoteEntityRepository {
        return RemoteEntityRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideEntityUseCases(
        localRepository: LocalEntityRepository,
        remoteRepository: RemoteEntityRepository,
        networkStatusTracker: NetworkStatusTracker
    ): EntityUseCases {
        return EntityUseCases(
            getAllEntitiesUseCase = GetAllEntitiesUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            getAllPropertiesUseCase = GetAllPropertiesUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            getEntitiesByPropertyUseCase = GetEntitiesByPropertyUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository
            ),
            getEntityUseCase = GetEntityUseCase(
                localRepository,
                remoteRepository = remoteRepository
            ),
            addEntityUseCase = AddEntityUseCase(
                localRepository = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            deleteEntityUseCase = DeleteEntityUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
            updateEntityUseCase = UpdateEntityUseCase(
                localRepository  = localRepository,
                remoteRepository = remoteRepository,
                networkStatusTracker = networkStatusTracker
            ),
//            enrollInEventUseCase = EnrollInEventUseCase(
//                localRepository  = localRepository,
//                remoteRepository = remoteRepository,
//                networkChecker = networkChecker
//            ),
//            getInProgressEventsUseCase = GetInProgressEventsUseCase(
//                localRepository  = localRepository,
//                remoteRepository = remoteRepository,
//                networkChecker = networkChecker
//            )
        )
    }
}