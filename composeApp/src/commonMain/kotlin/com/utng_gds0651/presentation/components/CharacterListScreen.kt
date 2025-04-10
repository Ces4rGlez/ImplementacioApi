package com.utng_gds0651.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.utng_gds0651.data.model.Character
import com.utng_gds0651.presentation.CharacterListViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterListScreen(
    onCharacterSelected: (Character) -> Unit
) {
    val viewModel = remember { CharacterListViewModel() }
    val state by viewModel.state.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var showFilterDialog by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Animated portal effect
    var portalRotation by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while(true) {
            delay(50)
            portalRotation += 0.5f
        }
    }

    // Animation states
    val headerHeight by animateFloatAsState(
        targetValue = if (isSearchFocused) 80f else 180f,
        animationSpec = tween(300, easing = FastOutSlowInEasing)
    )
    RickAndMortyTheme {
        Box(
            modifier = Modifier.fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column {
                // Animated Header with Portal Effect
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF3D996A), // Rick's portal green
                                    Color(0xFF97CE4C)  // More vibrant green
                                )
                            )
                        )
                ) {
                    // Add portal-like circular gradient in background with rotation
                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = 70.dp)
                            .graphicsLayer {
                                alpha = 0.7f
                                rotationZ = portalRotation
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFF7DECD3), // Inner portal color
                                        Color(0xFF3D996A), // Middle portal color
                                        Color(0xFF97CE4C).copy(alpha = 0f) // Outer fading
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    // Swirling effect inside portal
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .align(Alignment.CenterEnd)
                            .offset(x = 70.dp)
                            .graphicsLayer {
                                alpha = 0.5f
                                rotationZ = -portalRotation * 1.5f
                            }
                            .background(
                                brush = Brush.radialGradient(
                                    colors = listOf(
                                        Color.White,
                                        Color(0xFF7DECD3).copy(alpha = 0f)
                                    )
                                ),
                                shape = CircleShape
                            )
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 8.dp)
                    ) {
                        Text(
                            text = "Rick and Morty",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 34.sp,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )

                        AnimatedVisibility(
                            visible = !isSearchFocused,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Text(
                                text = "Multiverso de Personajes",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 18.sp,
                                    letterSpacing = 1.sp
                                ),
                                color = Color.White.copy(alpha = 0.9f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Search Bar with improved design
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .offset(y = (-24).dp)
                ) {
                    TextField(
                        value = searchQuery,
                        onValueChange = { newValue ->
                            searchQuery = newValue
                            viewModel.filterCharacters(newValue)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .shadow(12.dp, RoundedCornerShape(28.dp))
                            .clip(RoundedCornerShape(28.dp))
                            .onFocusChanged {
                                isSearchFocused = it.isFocused
                            },
                        shape = RoundedCornerShape(28.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        placeholder = {
                            Text("Buscar entre dimensiones...")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Buscar",
                                tint = Color(0xFF3D996A),
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = {
                                    searchQuery = ""
                                    viewModel.filterCharacters("")
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Limpiar búsqueda",
                                        tint = Color.Gray
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                viewModel.filterCharacters(searchQuery)
                                // Close keyboard
                                focusManager.clearFocus() // Correcto
                            }
                        )
                    )

                    // Filter button
                    FloatingActionButton(
                        onClick = { showFilterDialog = true },
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(56.dp)
                            .shadow(8.dp, CircleShape),
                        containerColor = Color.White,
                        contentColor = Color(0xFF3D996A)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filtrar",
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    state.isLoading -> {
                        LoadingView()
                    }
                    state.error != null -> {
                        ErrorView(errorMessage = state.error ?: "Error desconocido") {
                            viewModel.loadCharacters()
                        }
                    }
                    else -> {
                        CharactersContent(
                            characters = state.characters,
                            onCharacterClick = { character ->
                                coroutineScope.launch {
                                    viewModel.selectCharacter(character)
                                    onCharacterSelected(character)
                                }
                            },
                            onPreviousPage = { viewModel.loadPreviousPage() },
                            onNextPage = { viewModel.loadNextPage() }
                        )
                    }
                }
            }
        }

        // Filter Dialog
        if (showFilterDialog) {
            AlertDialog(
                onDismissRequest = { showFilterDialog = false },
                title = { Text("Filtrar personajes") },
                text = {
                    Column {
                        Text("Implementar opciones de filtrado por:")
                        Spacer(modifier = Modifier.height(4.dp))
                        FilterChip(
                            selected = false,
                            onClick = { /* Implementar filtro */ },
                            label = { Text("Estado (Vivo/Muerto)") },
                            leadingIcon = {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF3D996A), CircleShape)
                                )
                            }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FilterChip(
                            selected = false,
                            onClick = { /* Implementar filtro */ },
                            label = { Text("Especie") }
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        FilterChip(
                            selected = false,
                            onClick = { /* Implementar filtro */ },
                            label = { Text("Género") }
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text("Aplicar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showFilterDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingView() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        // Portal animation for loading
        val portalRotation = remember { mutableStateOf(0f) }

        LaunchedEffect(Unit) {
            while(true) {
                delay(16)
                portalRotation.value += 1f
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Custom portal loading animation
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .graphicsLayer {
                            rotationZ = portalRotation.value
                        }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFF7DECD3),
                                    Color(0xFF3D996A),
                                    Color(0xFF97CE4C).copy(alpha = 0.3f)
                                )
                            ),
                            shape = CircleShape
                        )
                )

                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .graphicsLayer {
                            rotationZ = -portalRotation.value * 1.5f
                        }
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White,
                                    Color(0xFF7DECD3).copy(alpha = 0f)
                                )
                            ),
                            shape = CircleShape
                        )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Atravesando el portal...",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.5.sp
                ),
                color = Color(0xFF3D996A)
            )
        }
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Error image using a portal effect
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(150.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Red.copy(alpha = 0.7f),
                                Color(0xFF3D996A).copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Text(
                text = "!",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "¡Wubba Lubba Dub Dub!",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF3D996A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF3D996A)
            ),
            modifier = Modifier
                .shadow(8.dp, RoundedCornerShape(20.dp))
                .height(56.dp)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            Text(
                "Intentar de nuevo",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun rememberFocusState(): MutableState<Boolean> {
    val state = remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    DisposableEffect(Unit) {
        onDispose {
            focusManager.clearFocus()
        }
    }

    return state
}

@Composable
fun CharactersContent(
    characters: List<Character>,
    onCharacterClick: (Character) -> Unit,
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit
) {
    AnimatedVisibility(
        visible = characters.isNotEmpty(),
        enter = fadeIn(tween(300)) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(400, easing = FastOutSlowInEasing)
        ),
        exit = fadeOut()
    ) {
        Column {
            Text(
                text = "Personajes del Multiverso",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                color = MaterialTheme.colorScheme.onBackground
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                items(characters) { character ->
                    CharacterGridItem(
                        character = character,
                        onClick = { onCharacterClick(character) }
                    )
                }
            }

            // Enhanced Pagination controls
            PaginationControls(
                onPreviousPage = onPreviousPage,
                onNextPage = onNextPage
            )
        }
    }
}

@Composable
fun PaginationControls(
    onPreviousPage: () -> Unit,
    onNextPage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            TextButton(
                onClick = onPreviousPage,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF3D996A)
                )
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowLeft,
                    contentDescription = "Anterior",
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    "Anterior",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                )
            }

            Divider(
                modifier = Modifier
                    .height(32.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )

            TextButton(
                onClick = onNextPage,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF3D996A)
                )
            ) {
                Text(
                    "Siguiente",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                )
                Icon(
                    Icons.Rounded.KeyboardArrowRight,
                    contentDescription = "Siguiente",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}

@Composable
fun CharacterGridItem(
    character: Character,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable {
                isPressed = true
                onClick()
            }
            .graphicsLayer {
                // Add subtle scale animation when pressed
                scaleX = if (isPressed) 0.96f else 1f
                scaleY = if (isPressed) 0.96f else 1f
            }
            .shadow(12.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Character image
            KamelImage(
                resource = asyncPainterResource(data = character.image),
                contentDescription = character.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Status indicator with glow effect
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(16.dp)
                    .shadow(4.dp, CircleShape)
                    .background(
                        color = when(character.status.lowercase()) {
                            "alive" -> Color(0xFF3D996A)
                            "dead" -> Color.Red
                            else -> Color.Gray
                        },
                        shape = CircleShape
                    )
                    .graphicsLayer {
                        // Add subtle glow for alive characters
                        if (character.status.lowercase() == "alive") {
                            alpha = 0.9f
                        }
                    }
            )

            // Character info overlay with improved gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f),
                                Color.Black.copy(alpha = 0.9f)
                            ),
                            startY = 0f,
                            endY = 200f
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            letterSpacing = 0.3.sp
                        ),
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = character.species,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(4.dp)
                                .background(Color.White.copy(alpha = 0.5f), CircleShape)
                        )

                        Text(
                            text = character.gender,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Status with animated glow for alive characters
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    when(character.status.lowercase()) {
                                        "alive" -> Color(0xFF3D996A)
                                        "dead" -> Color.Red
                                        else -> Color.Gray
                                    },
                                    CircleShape
                                )
                                .let {
                                    if (character.status.lowercase() == "alive") {
                                        it.shadow(2.dp, CircleShape, clip = false)
                                    } else {
                                        it
                                    }
                                }
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = character.status,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = when(character.status.lowercase()) {
                                "alive" -> Color(0xFF7DECD3)
                                "dead" -> Color.Red.copy(alpha = 0.9f)
                                else -> Color.White.copy(alpha = 0.8f)
                            }
                        )
                    }
                }
            }
        }
    }

    // Reset press state after animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(150)
            isPressed = false
        }
    }
}