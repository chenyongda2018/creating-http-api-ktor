package com.jetbrains.handson.httpapi.routes

import com.jetbrains.handson.httpapi.models.orderStorage
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.listOrderRoute() {
    get("/order") {
        if (orderStorage.isNotEmpty()) {
            call.respond(orderStorage)
        } else {
            call.respondText("Orders not found.", status = HttpStatusCode.NotFound)
        }
    }
}

fun Route.getOrderRoute() {
    get("/order/{id}") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)

        if (orderStorage.isNotEmpty()) {
            val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
                "The order is not exist.",
                status = HttpStatusCode.NotFound
            )

            call.respond(order)
        } else {
            call.respondText("There are no orders", status = HttpStatusCode.BadRequest)
        }
    }
}

fun Route.totalizeOrderRoute() {
    get("/order/{id}/total") {
        val id = call.parameters["id"] ?: return@get call.respondText("Bad request", status = HttpStatusCode.BadRequest)

        val order = orderStorage.find { it.number == id } ?: return@get call.respondText(
            "The order is not exist.",
            status = HttpStatusCode.NotFound
        )

        val total = order.contents.map { it.price * it.amount}.sum()

        call.respond(total)
    }
}

fun Application.registerOrderRoutes() {
    routing {
        listOrderRoute()

        getOrderRoute()

        totalizeOrderRoute()
    }
}