package com.example.plugins

import freemarker.cache.*
import freemarker.core.HTMLOutputFormat
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

/**
 * Esta función configura el motor de plantillas para el servidor web, que en este caso es
 * FreeMarker.
 */
fun Application.configureTemplating() {
    install(FreeMarker) {
        templateLoader = ClassTemplateLoader(this::class.java.classLoader, "templates")
        outputFormat = HTMLOutputFormat.INSTANCE
    }
    routing {
        get("/html-freemarker") {
            call.respond(FreeMarkerContent("index.ftl", mapOf("data" to IndexData(listOf(1, 2, 3))), ""))
        }
    }
}

/**
 * @property items List<Int>  lista de números enteros.
 * Esta clase sirve para encapsular datos que serán usados en el índice de una lista,
 * por ejemplo, en una paginación.
 */
data class IndexData(val items: List<Int>)
