package com.andreeailie.reexam.feature.data.repository

import android.util.Log
import com.andreeailie.reexam.feature.data.data_source.local.EntityDao
import com.andreeailie.reexam.feature.domain.model.Entity
import com.andreeailie.reexam.feature.domain.model.Property
import com.andreeailie.reexam.feature.domain.repository.LocalEntityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class LocalEntityRepositoryImpl(
    private val dao: EntityDao
) : LocalEntityRepository {

    override fun getAllEntities(): Flow<List<Entity>> {
        return dao.getAllEntities()
    }

    override fun getAllProperties(): Flow<List<Property>> {
        return dao.getAllProperties()
    }

    override suspend fun getEntityById(id: Int): Entity? {
        return dao.getEntityById(id)
    }

    override suspend fun getEntitiesByProperty(property: Property): Flow<List<Entity>> {
       return dao.getEntitiesByProperty(property.name)
    }

   override suspend fun findEntityByAttributes(title: String, author: String, isbn: String): Entity? {
        return dao.findEntityByAttributes(title, author, isbn)
    }

    override suspend fun insertEntity(entity: Entity) {
        dao.insertEntity(entity)
    }

    override suspend fun deleteEntity(entity: Entity) {
        dao.deleteEntity(entity)
    }

    override suspend fun deleteAllEntities() {
        dao.deleteAllEntities()
    }

    override suspend fun deleteAllProperties() {
        dao.deleteAllProperties()
    }

    override suspend fun getEventsWithPendingActions(): List<Entity> {
        return dao.getEntitiesWithPendingActions()
    }

    override suspend fun clearAndCacheEntities(entitiesFlow: Flow<List<Entity>>) {
        Log.d("LocalEntityRepositoryImpl", "clear and cache entities")
        dao.deleteAllEntities()

        entitiesFlow.first().forEach { entity ->
            //val entityCopy = entity.copy(ISBN = "1234")
            Log.d("LocalEntityRepositoryImpl", "entity: $entity")
            insertEntity(entity)
            Log.i("LocalEntityRepositoryImpl", "entity inserted")
        }

        val entitiesFromDb = dao.getAllEntities()
        Log.d("LocalEntityRepositoryImpl", "entities from db: $entitiesFromDb")
    }

    override suspend fun clearAndCacheProperties(propertiesFlow: Flow<List<Property>>) {
        dao.deleteAllProperties()

        propertiesFlow.first().forEach { property ->
            Log.d("LocalEntityRepositoryImpl", "property: $property")
            dao.insertProperty(property)

        }

        val propertiesFromDb = dao.getAllProperties()
        Log.d("LocalEntityRepositoryImpl", "properties from db: $propertiesFromDb")
    }
}