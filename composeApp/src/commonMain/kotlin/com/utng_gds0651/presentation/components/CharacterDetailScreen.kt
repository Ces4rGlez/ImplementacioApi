package com.utng_gds0651.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.utng_gds0651.data.model.Character
import com.utng_gds0651.presentation.CharacterListViewModel
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailScreen(
    character: Character,
    viewModel: CharacterListViewModel,
    onBackPressed: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val episode = state.firstEpisode
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var isFavorite by remember { mutableStateOf(false) }

    RickAndMortyTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                // Custom app bar with character image as background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                ) {
                    // Blurred background image for a more modern look
                    KamelImage(
                        resource = asyncPainterResource(data = character.image),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(radius = 3.dp)
                    )

                    // Clear image overlay
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )

                    // Main character image - centered and elevated
                    Box(
                        modifier = Modifier
                            .size(220.dp)
                            .align(Alignment.Center)
                            .padding(top = 20.dp)
                    ) {
                        KamelImage(
                            resource = asyncPainterResource(data = character.image),
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .align(Alignment.Center)
                        )

                        // Animated status indicator with pulsing effect
                        val pulseAnim = rememberInfiniteTransition()
                        val pulseSize by pulseAnim.animateFloat(
                            initialValue = 1.0f,
                            targetValue = if (character.status.lowercase() == "alive") 1.15f else 1.0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        val statusColor = when(character.status.lowercase()) {
                            "alive" -> Color(0xFF4CAF50)
                            "dead" -> Color(0xFFF44336)
                            else -> Color(0xFF9E9E9E)
                        }

                        Box(
                            modifier = Modifier
                                .size(24.dp * pulseSize)
                                .align(Alignment.BottomEnd)
                                .clip(CircleShape)
                                .background(statusColor)
                                .border(
                                    width = 3.dp,
                                    color = Color.White,
                                    shape = CircleShape
                                )
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Back button
                        IconButton(
                            onClick = onBackPressed,
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Atrás",
                                tint = Color.White
                            )
                        }

                        // Favorite button with animation
                        IconButton(
                            onClick = {
                                isFavorite = !isFavorite
                                scope.launch {
                                    val message = if (isFavorite)
                                        "¡${character.name} añadido a favoritos!"
                                    else
                                        "Eliminado de favoritos"
                                    snackbarHostState.showSnackbar(message)
                                }
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            val transition = updateTransition(isFavorite, label = "Favorite transition")
                            val scale by transition.animateFloat(
                                transitionSpec = { spring(stiffness = 900f) },
                                label = "Scale animation"
                            ) { if (it) 1.2f else 1f }
                            val color by transition.animateColor(
                                transitionSpec = { tween(300) },
                                label = "Color animation"
                            ) { if (it) Color(0xFFFF4081) else Color.White }

                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = "Favorito",
                                tint = color,
                                modifier = Modifier.scale(scale)
                            )
                        }
                    }

                    // Character name and status at bottom
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp, start = 24.dp, end = 24.dp)
                    ) {
                        Text(
                            text = character.name,
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.7f),
                                    offset = Offset(1f, 1f),
                                    blurRadius = 3f
                                )
                            ),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Status text
                        Text(
                            text = character.status.uppercase(),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Medium,
                            color = when(character.status.lowercase()) {
                                "alive" -> Color(0xFF4CAF50)
                                "dead" -> Color(0xFFF44336)
                                else -> Color(0xFF9E9E9E)
                            }
                        )
                    }
                }
            }
        ) { paddingValues ->
            // Character details with improved card layout and animations
            val scrollState = rememberScrollState()

            Surface(
                color = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                shadowElevation = 16.dp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 24.dp)
                ) {
                    // Character stats in a row
                    StatsRow(character)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Episode appearance with better visuals
                    EpisodeCard(character)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Location information with enhanced design
                    LocationSection(character)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Origin information with enhanced design
                    OriginSection(character)
                }
            }
        }
    }
}

@Composable
private fun StatsRow(character: Character) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCardImproved(
            icon = Icons.Outlined.Person,
            title = "Especie",
            value = character.species,
            modifier = Modifier.weight(1f),
            iconTint = MaterialTheme.colorScheme.primary
        )

        InfoCardImproved(
            icon = Icons.Outlined.Male,
            title = "Género",
            value = character.gender,
            modifier = Modifier.weight(1f),
            iconTint = MaterialTheme.colorScheme.secondary
        )

        InfoCardImproved(
            icon = Icons.Outlined.Tv,
            title = "Episodios",
            value = character.episode.size.toString(),
            modifier = Modifier.weight(1f),
            iconTint = MaterialTheme.colorScheme.tertiary
        )
    }
}

@Composable
private fun EpisodeCard(character: Character) {
    val randomEpisode = character.episode.firstOrNull()?.split("/")?.lastOrNull()?.toIntOrNull() ?: 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            SectionTitle(
                text = "Información de Episodios",
                icon = Icons.Outlined.MovieFilter
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Episode count with visual counter
                CircularProgressWithLabel(
                    maxValue = 51f,  // Total episodes in series approximately
                    value = character.episode.size.toFloat(),
                    label = character.episode.size.toString(),
                    size = 64.dp,
                    progressColor = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Apariciones Totales",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Primer episodio: S1E$randomEpisode",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationSection(character: Character) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SectionTitle(
                    text = "Última Localización",
                    icon = Icons.Outlined.LocationOn
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Location icon in circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = "Localización",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = character.location.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Última ubicación conocida",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OriginSection(character: Character) {
    AnimatedVisibility(
        visible = true,
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                SectionTitle(
                    text = "Origen",
                    icon = Icons.Outlined.Public
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Origin icon in circle
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Outlined.Public,
                            contentDescription = "Origen",
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = character.origin.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Planeta de origen",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCardImproved(
    icon: ImageVector,
    title: String,
    value: String,
    iconTint: Color = MaterialTheme.colorScheme.primary,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(0.9f),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconTint.copy(alpha = 0.1f))
            ) {
                Icon(
                    icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CircularProgressWithLabel(
    maxValue: Float,
    value: Float,
    label: String,
    size: Dp = 64.dp,
    strokeWidth: Dp = 6.dp,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        CircularProgressIndicator(
            progress = { value / maxValue },
            modifier = Modifier.fillMaxSize(),
            strokeWidth = strokeWidth,
            trackColor = trackColor,
            color = progressColor
        )

        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionTitle(
    text: String,
    icon: ImageVector? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        if (icon != null) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

// Extensión para facilitar el borde
fun Modifier.border(width: Dp, color: Color, shape: androidx.compose.ui.graphics.Shape) =
    this.then(Modifier.clip(shape).background(color))