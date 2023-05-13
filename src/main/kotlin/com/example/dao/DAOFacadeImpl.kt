package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class DAOFacadeImpl : DAOFacade {
    private fun resultRowToEntity(row: ResultRow) = Entity(
        id = row[Entities.id],
        value = row[Entities.value],
        name = row[Entities.name],
        description = row[Entities.description],
        sectionId = row[Entities.sectionId],
        order = row[Entities.order]
    )
    override suspend fun allEntities(): List<Entity> = dbQuery {
        Entities.selectAll().map(::resultRowToEntity)
    }

    override suspend fun entity(id: Int): Entity? = dbQuery {
        Entities
            .select { Entities.id eq id }
            .map(::resultRowToEntity)
            .singleOrNull()
    }

    override suspend fun addNewEntity(
        value: String,
        name: String,
        description: String,
        sectionId: String,
        order: Int): Entity? = dbQuery {
        val insertStatement = Entities.insert {
            it[Entities.value] = value
            it[Entities.name] = name
            it[Entities.description] = description
            it[Entities.sectionId] = sectionId
            it[Entities.order] = order
        }
        insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToEntity)
    }

    override suspend fun editEntity(id: Int,
                                    value: String,
                                    name: String,
                                    description: String,
                                    sectionId: String,
                                    order: Int): Boolean = dbQuery {
        Entities.update({ Entities.id eq id }) {
            it[Entities.value] = value
            it[Entities.name] = name
            it[Entities.description] = description
            it[Entities.sectionId] = sectionId
            it[Entities.order] = order
        } > 0
    }

    override suspend fun deleteEntity(id: Int): Boolean = dbQuery {
        Entities.deleteWhere { Entities.id eq id } > 0
    }
}




/*
    Se inicializa dao con una instancia de DAOFacadeImpl y se asegura de que haya al menos
    un item en la base de datos.
 */
val dao: DAOFacade = DAOFacadeImpl().apply {
    runBlocking {
        if(allEntities().isEmpty()) {
            addNewEntity("1024,43", "Iphone 14", "Es un móvil increíble con unas características imbatibles", "A", 10 )
        }
    }
}