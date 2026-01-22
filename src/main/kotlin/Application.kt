package com

import com.model.FakeUserRepository
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val repository = FakeUserRepository()

    configureSerialization(repository)
    configureDatabases()
    configureRouting()
}
