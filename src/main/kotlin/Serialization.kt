package com

import com.model.Frequency
import com.model.User
import com.model.UserRepository
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSerialization(repository: UserRepository) {
    install(ContentNegotiation) {
        json()
    }
    routing {
        route("/users") {
            get {
                val users = repository.allUsers()
                call.respond(users)
            }

            get("/byName/{userDisplayName}") {
                val name = call.parameters["userDisplayName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                val user = repository.userByName(name)
                if (user == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }
                call.respond(user)
            }

            get("/byFrequency/{frequency}") {
                val frequencyAsText = call.parameters["frequency"]
                if (frequencyAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }
                try {
                    val frequency = Frequency.valueOf(frequencyAsText)
                    val users = repository.usersByFrequency(frequency)

                    if (users.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }
                    call.respond(users)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            post {
                try {
                    val user = call.receive<User>()
                    repository.addUser(user)
                    call.respond(HttpStatusCode.NoContent)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: JsonConvertException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{userId}") {
                val id = call.parameters["userId"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }
                if (repository.removeUser(id)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }
        }
    }
}
