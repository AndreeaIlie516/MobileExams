package com.andreeailie.exam.feature.data.repository

import android.util.Log
import com.andreeailie.exam.feature.data.data_source.local.EntityDao
import com.andreeailie.exam.feature.domain.model.Entity
import com.andreeailie.exam.feature.domain.model.Property
import com.andreeailie.exam.feature.domain.repository.LocalEntityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalEntityRepositoryImpl(
    private val dao: EntityDao
) : LocalEntityRepository {

    override fun getAllEntities(): Flow<List<Entity>> {
        return dao.getAllEvents()
    }

    override suspend fun getEntityById(id: Int): Entity? {
        return dao.getEntityById(id)
    }


    override suspend fun insertEntity(entity: Entity) {
        dao.insertEvent(entity)
    }

    override suspend fun deleteEntity(entity: Entity) {
        dao.deleteEvent(entity)
    }

    override suspend fun deleteAllEntities() {
        dao.deleteAllEntities()
    }

    override suspend fun clearAndCacheEntities(entitiesFlow: Flow<List<Entity>>) {
        dao.deleteAllEntities()

        entitiesFlow.first().forEach { entity ->
            Log.d("LocalEntityRepositoryImpl", "entity: $entity")
            insertEntity(entity)

        }

        val entitiesFromDb = dao.getAllEvents()
        Log.d("LocalEntityRepositoryImpl", "entities from db: $entitiesFromDb")
    }

    override suspend fun getEventsWithPendingActions(): List<Entity> {
        return dao.getEventsWithPendingActions()
    }
}