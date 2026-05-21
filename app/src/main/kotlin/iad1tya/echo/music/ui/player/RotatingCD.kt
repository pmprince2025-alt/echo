@file:OptIn(ExperimentalMaterial3Api::class)

package iad1tya.echo.music.ui.player

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import iad1tya.echo.music.models.MediaMetadata
import iad1tya.echo.music.utils.ImageUtils

@Composable
fun RotatingCDArtwork(
    mediaMetadata: MediaMetadata?,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            rotation.animateTo(
                targetValue = rotation.value + 36000f,
                animationSpec = tween(
                    durationMillis = (800000f * (1f - rotation.value / 36000f)).toLong().coerceAtLeast(8000),
                    easing = LinearEasing
                )
            )
        }
    }

    val displayRotation by animateFloatAsState(
        targetValue = rotation.value,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioNoBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "cdDisplayRotation"
    )

    val thumbnailUrl = mediaMetadata?.thumbnailUrl
    val highResUrl = remember(thumbnailUrl) {
        ImageUtils.getHighResThumbnailUrl(thumbnailUrl, 1920)
    }

    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        val cdSize = minOf(maxWidth, maxHeight)
        val holeRadius = cdSize * 0.09f

        // Outer glow ring
        Box(
            modifier = Modifier
                .size(cdSize)
                .graphicsLayer {
                    alpha = 0.25f
                    scaleX = 1.05f
                    scaleY = 1.05f
                    compositingStrategy = CompositingStrategy.Offscreen
                }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.10f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = size.minDimension / 2f
                            )
                        )
                    }
                }
        ) {
            // Blurred backdrop for depth
            if (highResUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(highResUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            alpha = 0.25f
                            scaleX = 1.15f
                            scaleY = 1.15f
                        }
                )
            }
        }

        // Main CD disc
        Box(
            modifier = Modifier
                .size(cdSize)
                .graphicsLayer {
                    rotationZ = if (isPlaying) displayRotation else 0f
                    shadowElevation = 16.dp.toPx()
                    shape = CircleShape
                    clip = true
                    ambientShadowColor = Color.Black.copy(alpha = 0.4f)
                    spotShadowColor = Color.Black.copy(alpha = 0.3f)
                }
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()

                        // Vignette edge
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.40f)
                                ),
                                center = center,
                                radius = size.minDimension / 2f
                            )
                        )

                        // Groove rings
                        val maxR = size.minDimension / 2f
                        val grooveColor = Color.White.copy(alpha = 0.05f)
                        for (i in 1..8) {
                            val r = maxR * (0.12f + i * 0.10f)
                            drawCircle(color = grooveColor, radius = r, center = center)
                        }

                        // Center hole via DstOut
                        drawCircle(
                            color = Color.Transparent,
                            radius = holeRadius.toPx(),
                            center = center,
                            blendMode = BlendMode.DstOut
                        )

                        // Dark ring around hole
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.5f),
                                    Color.Transparent
                                ),
                                center = center,
                                radius = holeRadius.toPx() * 1.5f
                            ),
                            radius = holeRadius.toPx() * 1.5f,
                            center = center
                        )
                    }
                }
        ) {
            // Album art
            if (highResUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(highResUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1A1A1A))
                )
            }

            // Glass shine reflection
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithCache {
                        val colors = listOf(
                            Color.White.copy(alpha = 0.22f),
                            Color.White.copy(alpha = 0.08f),
                            Color.Transparent,
                            Color.Transparent,
                            Color.White.copy(alpha = 0.06f),
                            Color.Transparent,
                        )
                        val brush = Brush.linearGradient(
                            colors = colors,
                            start = Offset.Zero,
                            end = Offset(size.width, size.height)
                        )
                        onDrawWithContent {
                            drawContent()
                            drawRect(brush = brush, blendMode = BlendMode.Screen)
                        }
                    }
            )
        }

        // Subtle rotating sweep light
        if (isPlaying) {
            Box(
                modifier = Modifier
                    .size(cdSize)
                    .graphicsLayer {
                        rotationZ = displayRotation * 0.5f
                        alpha = 0.06f
                        compositingStrategy = CompositingStrategy.Offscreen
                    }
                    .drawWithCache {
                        val brush = Brush.sweepGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.White.copy(alpha = 0.6f),
                                Color.Transparent,
                                Color.Transparent,
                            ),
                            center = center
                        )
                        onDrawBehind {
                            drawCircle(brush = brush)
                        }
                    }
            )
        }
    }
}
