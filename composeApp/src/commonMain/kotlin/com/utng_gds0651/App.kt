package com.utng_gds0651

import androidx.compose.runtime.*
import com.utng_gds0651.data.model.Character
import com.utng_gds0651.presentation.CharacterListViewModel
import com.utng_gds0651.presentation.components.CharacterDetailScreen
import com.utng_gds0651.presentation.components.CharacterListScreen
import com.utng_gds0651.presentation.components.RickAndMortyTheme

@Composable
fun App() {
    var selectedCharacter by remember { mutableStateOf<Character?>(null) }
    val viewModel = remember { CharacterListViewModel() }

    RickAndMortyTheme {
        if (selectedCharacter == null) {
            CharacterListScreen(
                onCharacterSelected = { character ->
                    selectedCharacter = character
                }
            )
        } else {
            CharacterDetailScreen(
                character = selectedCharacter!!,
                viewModel = viewModel,
                onBackPressed = { selectedCharacter = null }
            )
        }
    }
}