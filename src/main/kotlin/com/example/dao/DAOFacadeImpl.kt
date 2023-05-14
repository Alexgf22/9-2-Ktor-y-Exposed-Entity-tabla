package com.example.dao

import com.example.dao.DatabaseFactory.dbQuery
import com.example.models.*
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/**
 * Esta clase implementa las funciones de la interfaz DAOFacade para poder añadir entity,
 * eliminar entity, editar entity, devolver todos los entities...
 */
class DAOFacadeImpl : DAOFacade {

    /**
     * @param row: ResultRow  fila de resultados  que es devuelta por una consulta a la
     * base de datos.
     * Al utilizar por ejemplo:  'row[Entities.id]'  lo que hace es acceder al valor de
     * la columna id de la tabla Entities y lo mismo con las otras variables.
     * Por lo que contruye el objeto Entity con los 6 valores de las columnas
     * id, value, name, description, sectionId y order  correspondientes.
     * @return Entity  retorna un objeto Entity con los valores de la fila.
     */
    private fun resultRowToEntity(row: ResultRow) = Entity(
        id = row[Entities.id],
        value = row[Entities.value],
        name = row[Entities.name],
        description = row[Entities.description],
        sectionId = row[Entities.sectionId],
        order = row[Entities.order]
    )

    /**
     * Esta función a través de la biblioteca de mapeo objeto-relacional "Exposed"
     * para ejecutar una consulta segura en la tabla "Entities". La consulta
     * selecciona todas las filas de la tabla y después se aplica una función map
     * a los resultados de la consulta para convertir cada fila de resultados en
     * un objeto Entity con la función resultRowToEntity.
     * @return List<Entity>  retorna una lista no mutable de objetos Entity.
     *
     */
    override suspend fun allEntities(): List<Entity> = dbQuery {
        Entities.selectAll().map(::resultRowToEntity)
    }


    /**
     * @param id: Int   se le pasa un identificador único por parámetro a la función.
     * La función entity es una función suspend que usa la función de orden superior
     * dbQuery para ejecutar la consulta de manera segura en la base de datos.
     *
     * La consulta selecciona una fila específica de la tabla "Entities" usando el
     * método select. La función usa el operador eq para comparar el valor de la
     * columna "id" de la tabla "Entities" con el argumento id.
     *
     * Después, se convierte cada fila de resultados en un objeto Entity con
     * la función resultRowToEntity y el método map de la clase Iterable.
     *
     * @return Entity?  retorna el único objeto Entity de la colección con el método singleOrNull
     * el cual retorna null si la colección está vacía o si contiene más de un elemento.
     */
    override suspend fun entity(id: Int): Entity? = dbQuery {
        Entities
            .select { Entities.id eq id }
            .map(::resultRowToEntity)
            .singleOrNull()
    }


    /**
     * @param value: String  valor del item.
     * @param name: String  nombre del item.
     * @param description: String  descripción del item.
     * @param sectionId: String  sectionId del item.
     * @param order: Int  cantidad de items.
     *
     * La función addNewEntity es una función suspend que usa la función de orden superior
     * dbQuery para ejecutar una consulta de manera segura en la base de datos.
     *
     * En la consulta, se usa el método insert proporcionado por la biblioteca "Exposed" para
     * añadir un nuevo registro en la tabla "Entities". Se asignan los valores del título y del
     * cuerpo mediante la función lambda it y los parámetros title y body.
     *
     * @return Entity?  retorna el nuevo registro como objeto Entity con el método let de la
     * clase ResultRow, aplica el resultado que saque la consulta con el operador de elvis. Si
     * la consulta no retorna nada, devolvería null.
     */
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


    /**
     * @param id: Int identificador único del item.
     * @param value: String  valor del item.
     * @param name: String  nombre del item.
     * @param description: String  descripción del item.
     * @param sectionId: String  sectionId del item.
     * @param order: Int  cantidad de items.
     *
     * La función editEntity es una función suspend que usa la función de orden superior dbQuery
     * para ejecutar una consulta de forma más segura en la base de datos.
     *
     * Dentro de dbQuery, la función ejecuta una consulta para actualizar una fila específica
     * de la tabla "Entities" utilizando el método update proporcionado por la biblioteca
     * "Exposed". La función utiliza el operador eq para comparar el valor de la columna "id"
     * de la tabla "Entities" con el argumento id.
     *
     * La función luego especifica los nuevos valores para las columnas "value", "name", "description",
     * "sectionId" y "order".
     * utilizando los operadores de asignación. La función utiliza el método > para comprobar
     * si la actualización afectó a alguna fila en la tabla.
     *
     * @return Boolean  retorna true si la actualización afectó a una fila o más, sino, retorna false.
     */
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


    /**
     * @param id: Int  identificador único del item.
     * La función deleteEntity es una función suspend que usa la función de orden superior dbQuery para
     * ejecutar una consulta de manera segura en la base de datos.
     *
     * La consulta elimina una fila específica de la tabla "Entities" mediante el método deleteWhere.
     * Además usa el operador eq para comparar el valor de la columna "id" de la tabla "Entities"
     * con el argumento id.
     *
     * @return Boolean  retorna true si por lo menos una fila es eliminada de la tabla "Entities" y
     * false en caso contrario.
     */
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