package com.utng_gds0651.presentation

import com.utng_gds0651.data.model.Character
import com.utng_gds0651.data.model.Episode
import com.utng_gds0651.data.repository.RickAndMortyRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterListViewModel {
    private val repository = RickAndMortyRepository()
    private val viewModelScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(CharacterListState())
    val state: StateFlow<CharacterListState> = _state.asStateFlow()

    private var allCharacters = listOf<Character>()
    private var currentPage = 1

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            try {
                repository.getCharacters(currentPage).collect { characters ->
                    allCharacters = characters // Almacena la lista completa
                    _state.update {
                        it.copy(
                            isLoading = false,
                            characters = characters,
                            error = null
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message
                    )
                }
            }
        }
    }

    fun filterCharacters(query: String) {
        if (query.isEmpty()) {
            // Reset to show all characters
            _state.update { it.copy(characters = allCharacters) }
        } else {
            // Filter based on search query
            val filteredList = allCharacters.filter { character ->
                character.name.contains(query, ignoreCase = true) ||
                        character.species.contains(query, ignoreCase = true) ||
                        character.status.contains(query, ignoreCase = true)
            }
            _state.update { it.copy(characters = filteredList) }
        }
    }

    fun loadNextPage() {
        currentPage++
        loadCharacters()
    }

    fun loadPreviousPage() {
        if (currentPage > 1) {
            currentPage--
            loadCharacters()
        }
    }

    fun selectCharacter(character: Character) {
        _state.update { it.copy(selectedCharacter = character) }
        loadFirstEpisode(character)
    }

    private fun loadFirstEpisode(character: Character) {
        if (character.episode.isNotEmpty()) {
            // Primero indicamos que estamos cargando el episodio
            _state.update { it.copy(firstEpisode = null) }

            viewModelScope.launch {
                try {
                    val episodeUrl = character.episode.first()
                    // Agrega un log para depuración
                    println("Intentando cargar episodio desde URL: $episodeUrl")

                    val episode = repository.getEpisode(episodeUrl)
                    if (episode != null) {
                        _state.update { it.copy(firstEpisode = episode) }
                        println("Episodio cargado correctamente: ${episode.name}")
                    } else {
                        println("Error: El episodio retornado es nulo")
                        _state.update { it.copy(error = "No se pudo cargar el episodio") }
                    }
                } catch (e: Exception) {
                    // Registra el error para depuración
                    println("Error al cargar el episodio: ${e.message}")
                    _state.update { it.copy(error = "Error al cargar el episodio: ${e.message}") }
                }
            }
        } else {
            // Si no hay episodios, actualizamos el estado para mostrar que no hay info
            _state.update { it.copy(firstEpisode = null) }
        }
    }

}

data class CharacterListState(
    val isLoading: Boolean = false,
    val characters: List<Character> = emptyList(),
    val selectedCharacter: Character? = null,
    val firstEpisode: Episode? = null,
    val error: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 0
)