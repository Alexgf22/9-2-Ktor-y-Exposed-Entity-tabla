package com.example.plugins

import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import kotlinx.serialization.Serializable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*

/**
 * La clase User representa a un usuario, con un nombre y una edad.
 * @property name : String   el nombre del usuario.
 * @property age : Int  la edad del usuario.
 */
@Serializable
data class User(val name: String, val age: Int)

/**
 * Clase que encapsula la funcionalidad de acceso a la base de datos para un recurso de usuario.
 * Se utiliza para realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) en la tabla
 * Users de la base de datos.
 * @property database : Database  La clase usa este objeto para realizar transacciones en
 * la base de datos.
 */
class UserService(private val database: Database) {
    object Users : Table() {
        val id = integer("id").autoIncrement()
        val name = varchar("name", length = 50)
        val age = integer("age")

        override val primaryKey = PrimaryKey(id)
    }

    init {
        transaction(database) {
            SchemaUtils.create(Users)
        }
    }

    /**
     * @param block: suspend () -> T   Esta función se usa para evitar la
     * repetición de código en las funciones de CRUD.
     */
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }


    /**
     * @param user: User  este objeto user es insertado en la tabla Users.
     * @return Int  retorna el id del nuevo registro insertado.
     */
    suspend fun create(user: User): Int = dbQuery {
        Users.insert {
            it[name] = user.name
            it[age] = user.age
        }[Users.id]
    }


    /**
     *  @param id: Int  elige este id de usuario.
     *  @return User  retorna este objeto de la base de datos si existe. En caso
     *  contrario, retorna null.
     */
    suspend fun read(id: Int): User? {
        return dbQuery {
            Users.select { Users.id eq id }
                .map { User(it[Users.name], it[Users.age]) }
                .singleOrNull()
        }
    }


    /**
     * @param id: Int  se le pasa a la función este identificador de usuario.
     * @param user: User  también se le pasa este objeto user.
     * En este caso actualiza el registro correspondiente en la base de datos.
     *
     */
    suspend fun update(id: Int, user: User) {
        dbQuery {
            Users.update({ Users.id eq id }) {
                it[name] = user.name
                it[age] = user.age
            }
        }
    }

    /**
     * @param id: Int  Se le pasa la función este identificador por parámetro.
     * Lo que hace es eliminar el registro correspondiente de la base de datos.
     */
    suspend fun delete(id: Int) {
        dbQuery {
            Users.deleteWhere { Users.id.eq(id) }
        }
    }
}