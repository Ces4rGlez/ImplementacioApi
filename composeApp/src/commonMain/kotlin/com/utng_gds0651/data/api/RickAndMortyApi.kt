package com.utng_gds0651.data.api


import com.utng_gds0651.data.model.Character
import com.utng_gds0651.data.model.CharacterResponse
import com.utng_gds0651.data.model.Episode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class RickAndMortyApi {
    private val baseUrl = "https://rickandmortyapi.com/api"

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
            })
        }
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    // Obtener una lista de personajes
    suspend fun getCharacters(page: Int = 1): CharacterResponse {
        return client.get("$baseUrl/character?page=$page").body()
    }

    // Obtener un personaje específico
    suspend fun getCharacter(id: Int): Character {
        return client.get("$baseUrl/character/$id").body()
    }

    // Obtener un episodio específico
    suspend fun getEpisode(url: String): Episode {
        return client.get(url).body()
    }

    fun close() {
        client.close()
    }
}
