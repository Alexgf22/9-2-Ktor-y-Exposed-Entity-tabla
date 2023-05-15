package com.example.plugins

import com.example.dao.dao
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*

/**
 * Esta función configura las rutas utilizadas por la app, incluyendo la redirección de la
 * ruta principal a la lista de items, el manejo de la creación, actualización y eliminación de items
 * y la visualización de un item específico.
 */
fun Application.configureRouting() {
    routing {// Función de enrutamiento que configura las rutas de la app

        /*
        redirige todas GET las solicitudes realizadas a la /ruta a /entities
         */
        get("/") {
            call.respondRedirect("entities")
        }

        route("entities") {

            /*
            FreeMarkerContent() , objeto que representa el contenido para ser enviado al cliente.
            Acepta dos parámetros: template: nombre de la plantilla, model: le pasamos una lista de archivos
             */
            get {
                call.respond(FreeMarkerContent("index.ftl", mapOf("entities" to dao.allEntities())))
            }

            get("new") {
                call.respond(FreeMarkerContent("new.ftl", model = null))
            }

            // Crea un nuevo item a partir de los parámetros enviados en el formulario.
            post {
                val formParameters = call.receiveParameters()
                val value = formParameters.getOrFail("value")
                val name = formParameters.getOrFail("name")
                val description = formParameters.getOrFail("description")
                val sectionId = formParameters.getOrFail("sectionId")
                val order = formParameters.getOrFail<Int>("order").toInt()
                val entity = dao.addNewEntity(value, name, description, sectionId, order)
                call.respondRedirect("/entities/${entity?.id}")
            }

            // Para mostrar el contenido de un item específico, se usa el ID del item como parámetro de ruta
            get("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("show.ftl", mapOf("entity" to dao.entity(id))))
            }
            /* Ruta para editar un item. ('call.parameters') se usa para obtener el identificador del item
             y encontrar este item en un almacén
             */
            get("{id}/edit") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("edit.ftl", mapOf("entity" to dao.entity(id))))
            }

            /*
            En primer lugar, con call.parameters, obtenemos el ID del item a editar.
            Con call.receiveParameters se usa para que un usuario inicie la acción (update o delete)
            Dependiendo de la acción, el item se actualiza o elimina del almacenamiento.
             */
            post("{id}") {
                val id = call.parameters.getOrFail<Int>("id").toInt()
                val formParameters = call.receiveParameters()
                when (formParameters.getOrFail("_action")) {
                    "update" -> {
                        val value = formParameters.getOrFail("value")
                        val name = formParameters.getOrFail("name")
                        val description = formParameters.getOrFail("description")
                        val sectionId = formParameters.getOrFail("sectionId")
                        val order = formParameters.getOrFail<Int>("order").toInt()
                        dao.editEntity(id, value, name, description, sectionId, order)
                        call.respondRedirect("/entities/$id")
                    }
                    "delete" -> {
                        dao.deleteEntity(id)
                        call.respondRedirect("/entities")
                    }
                }
            }
        }
    }
}



