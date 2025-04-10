package com.utng_gds0651.data.repository

import com.utng_gds0651.data.api.RickAndMortyApi
import com.utng_gds0651.data.model.Character
import com.utng_gds0651.data.model.Episode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RickAndMortyRepository {
    private val api = RickAndMortyApi()

    fun getCharacters(page: Int = 1): Flow<List<Character>> = flow {
        try {
            val response = api.getCharacters(page)
            emit(response.results)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    suspend fun getCharacter(id: Int): Character? {
        return try {
            api.getCharacter(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getEpisode(url: String): Episode? {
        return try {
            api.getEpisode(url)
        } catch (e: Exception) {
            null
        }
    }
}