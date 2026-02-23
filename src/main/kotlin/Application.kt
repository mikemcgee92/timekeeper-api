package com

import com.model.PostgresUserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = PostgresUserRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()
}
