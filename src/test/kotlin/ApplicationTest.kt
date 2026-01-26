package com

import com.model.Frequency
import com.model.User
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlin.test.*

class ApplicationTest {
    @Test
    fun usersCanBeFoundByFrequency() = testApplication {
        application {
            val repository = FakeUserRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val response = client.get("/users/byFrequency/Daily")
        val results = response.body<List<User>>()

        assertEquals(HttpStatusCode.OK, response.status)

        val expectedUserDisplayNames = listOf("Bobson Dugnutt", "Mike Truk")
        val actualUserDisplayNames = results.map(User::displayName)
        assertContentEquals(expectedUserDisplayNames, actualUserDisplayNames)
    }

    @Test
    fun invalidFrequencyProduces404() = testApplication {
        application {
            val repository = FakeUserRepository()
            configureSerialization(repository)
            configureRouting()
        }
        val response = client.get("/users/byFrequency/Invalid")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun unusedFrequencyProduces404() = testApplication {
        application {
            val repository = FakeUserRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val response = client.get("/users/byFrequency/Yearly")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newUsersCanBeAdded() = testApplication {
        application {
            val repository = FakeUserRepository()
            configureSerialization(repository)
            configureRouting()
        }

        val client = createClient {
            install(ContentNegotiation) {
                json()
            }
        }

        val user = User("Mafia Jimmy", "http://www.mafia.com/jimmy.png", 200.40, Frequency.Daily)
        val response1 = client.post("/users") {
            header(
                HttpHeaders.ContentType,
                ContentType.Application.Json
            )

            setBody(user)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)

        val response2 = client.get("/users")
        assertEquals(HttpStatusCode.OK, response2.status)

        val userNames = response2
            .body<List<User>>()
            .map { it.displayName }

        assertContains(userNames, "Mafia Jimmy")
    }
}
