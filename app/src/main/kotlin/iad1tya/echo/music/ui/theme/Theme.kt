package prince.sonic.music.ui.theme

import android.graphics.Bitmap
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.palette.graphics.Palette
import com.materialkolor.rememberDynamicColorScheme
import com.materialkolor.score.Score

object SonicColors {
    val ElectricBlue = Color(0xFF00D9FF)
    val ElectricBlueDark = Color(0xFF0099CC)
    val NeonPink = Color(0xFFFF6BD9)
    val DeepBlack = Color(0xFF0A0A0F)
    val DarkGray = Color(0xFF1A1A2E)
    val SurfaceDark = Color(0xFF12121A)
    val GlassSurface = Color(0xCC1A1A2E)
    val CyanGlow = Color(0x8000D9FF)
    val PinkGlow = Color(0x80FF6BD9)
    val OnSurfaceLight = Color(0xFFE0E0E0)
    val OutlineDark = Color(0xFF3A3A4A)
}

val DefaultThemeColor = SonicColors.ElectricBlue

private val DarkColorScheme = darkColorScheme(
    primary = SonicColors.ElectricBlue,
    onPrimary = SonicColors.DeepBlack,
    primaryContainer = SonicColors.DarkGray,
    onPrimaryContainer = SonicColors.ElectricBlue,
    secondary = SonicColors.NeonPink,
    onSecondary = SonicColors.DeepBlack,
    secondaryContainer = SonicColors.DarkGray,
    onSecondaryContainer = SonicColors.NeonPink,
    tertiary = Color(0xFF9D9DB5),
    onTertiary = SonicColors.DeepBlack,
    tertiaryContainer = SonicColors.DarkGray,
    onTertiaryContainer = Color(0xFF9D9DB5),
    error = Color(0xFFFF6B6B),
    onError = SonicColors.DeepBlack,
    errorContainer = Color(0xFF5A0000),
    onErrorContainer = Color(0xFFFFDAD6),
    background = SonicColors.DeepBlack,
    onBackground = Color.White,
    surface = SonicColors.DeepBlack,
    onSurface = Color.White,
    surfaceVariant = SonicColors.SurfaceDark,
    onSurfaceVariant = SonicColors.OnSurfaceLight,
    surfaceTint = SonicColors.ElectricBlue,
    inverseSurface = Color.White,
    inverseOnSurface = SonicColors.DeepBlack,
    outline = SonicColors.OutlineDark,
    outlineVariant = Color(0xFF2A2A3A),
    scrim = SonicColors.DeepBlack,
    surfaceBright = SonicColors.DarkGray,
    surfaceContainer = SonicColors.SurfaceDark,
    surfaceContainerHigh = SonicColors.DarkGray,
    surfaceContainerHighest = Color(0xFF252535),
    surfaceContainerLow = Color(0xFF0E0E14),
    surfaceContainerLowest = SonicColors.DeepBlack,
    surfaceDim = SonicColors.DeepBlack
)

private val LightColorScheme = lightColorScheme(
    primary = SonicColors.ElectricBlueDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F7FF),
    onPrimaryContainer = SonicColors.ElectricBlueDark,
    secondary = Color(0xFFE850A0),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE0F0),
    onSecondaryContainer = Color(0xFFE850A0),
    tertiary = Color(0xFF6B6B80),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFF0F0F5),
    onTertiaryContainer = Color(0xFF6B6B80),
    error = Color(0xFFD32F2F),
    onError = Color.White,
    errorContainer = Color(0xFFFFCDD2),
    onErrorContainer = Color(0xFF5A0000),
    background = Color.White,
    onBackground = SonicColors.DeepBlack,
    surface = Color.White,
    onSurface = SonicColors.DeepBlack,
    surfaceVariant = Color(0xFFF5F5F8),
    onSurfaceVariant = Color(0xFF3A3A4A),
    surfaceTint = SonicColors.ElectricBlueDark,
    inverseSurface = SonicColors.DarkGray,
    inverseOnSurface = Color.White,
    outline = Color(0xFFB0B0B8),
    outlineVariant = Color(0xFFE0E0E5),
    scrim = Color(0xFF000000),
    surfaceBright = Color.White,
    surfaceContainer = Color(0xFFF8F8FA),
    surfaceContainerHigh = Color(0xFFF0F0F5),
    surfaceContainerHighest = Color(0xFFE8E8ED),
    surfaceContainerLow = Color(0xFFFCFCFE),
    surfaceContainerLowest = Color.White,
    surfaceDim = Color(0xFFF0F0F5)
)

@Composable
fun SonicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    isDynamicColor: Boolean = false,
    useSystemFont: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val useSystemDynamic = isDynamicColor && themeColor == DefaultThemeColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val useCustomSeedColor = themeColor != DefaultThemeColor

    val colorScheme = when {
        useSystemDynamic -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        useCustomSeedColor -> {
            rememberDynamicColorScheme(
                seedColor = themeColor,
                isDark = darkTheme,
            )
        }
        else -> if (darkTheme) DarkColorScheme else LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme.pureBlack(pureBlack),
        typography = if (useSystemFont) Typography() else AppTypography,
        content = content
    )
}

@Deprecated("Use SonicTheme instead", ReplaceWith("SonicTheme"))
@Composable
fun EchoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    pureBlack: Boolean = false,
    themeColor: Color = DefaultThemeColor,
    isDynamicColor: Boolean = false,
    useSystemFont: Boolean = false,
    content: @Composable () -> Unit,
) = SonicTheme(darkTheme, pureBlack, themeColor, isDynamicColor, useSystemFont, content)

fun Bitmap.extractThemeColor(): Color {
    val colorsToPopulation = Palette.from(this)
        .maximumColorCount(8)
        .generate()
        .swatches
        .associate { it.rgb to it.population }
    val rankedColors = Score.score(colorsToPopulation)
    return Color(rankedColors.first())
}

fun Bitmap.extractGradientColors(): List<Color> {
    val extractedColors = Palette.from(this)
        .maximumColorCount(64)
        .generate()
        .swatches
        .associate { it.rgb to it.population }

    val orderedColors = Score.score(extractedColors, 2, 0xff4285f4.toInt(), true)
        .sortedByDescending { Color(it).luminance() }

    return if (orderedColors.size >= 2)
        listOf(Color(orderedColors[0]), Color(orderedColors[1]))
    else
        listOf(Color(0xFF595959), Color(0xFF0D0D0D))
}

fun ColorScheme.pureBlack(apply: Boolean) =
    if (apply) copy(
        surface = SonicColors.DeepBlack,
        background = SonicColors.DeepBlack
    ) else this

val ColorSaver = object : Saver<Color, Int> {
    override fun restore(value: Int): Color = Color(value)
    override fun SaverScope.save(value: Color): Int = value.toArgb()
}