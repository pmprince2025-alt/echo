package prince.sonic.music.ui.screens

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import prince.sonic.music.R
import prince.sonic.music.constants.DarkModeKey
import prince.sonic.music.ui.component.BottomSheet
import prince.sonic.music.ui.component.BottomSheetMenu
import prince.sonic.music.ui.component.LocalMenuState
import prince.sonic.music.ui.component.rememberBottomSheetState
import prince.sonic.music.ui.player.AmbientModeScreen
import prince.sonic.music.ui.screens.BrowseScreen
import prince.sonic.music.ui.screens.artist.ArtistAlbumsScreen
import prince.sonic.music.ui.screens.artist.ArtistItemsScreen
import prince.sonic.music.ui.screens.artist.ArtistScreen
import prince.sonic.music.ui.screens.artist.ArtistSongsScreen
import prince.sonic.music.ui.screens.library.LibraryScreen
import prince.sonic.music.ui.player.VideoPlayerScreen
import prince.sonic.music.ui.screens.playlist.AutoPlaylistScreen
import prince.sonic.music.ui.screens.playlist.LocalPlaylistScreen
import prince.sonic.music.ui.screens.playlist.OnlinePlaylistScreen
import prince.sonic.music.ui.screens.playlist.TopPlaylistScreen
import prince.sonic.music.ui.screens.podcast.OnlinePodcastScreen
import prince.sonic.music.ui.screens.playlist.CachePlaylistScreen
import prince.sonic.music.ui.screens.search.OnlineSearchResult
import prince.sonic.music.ui.screens.WrappedScreen
import prince.sonic.music.ui.screens.settings.AboutScreen
import prince.sonic.music.ui.screens.settings.AccountSettings
import prince.sonic.music.ui.screens.settings.AppearanceSettings
import prince.sonic.music.ui.screens.settings.BackupAndRestore
import prince.sonic.music.ui.screens.settings.ContentSettings
import prince.sonic.music.ui.screens.settings.DarkMode
import prince.sonic.music.ui.screens.settings.PlayerSettings
import prince.sonic.music.ui.screens.settings.PoTokenScreen
import prince.sonic.music.ui.screens.settings.PrivacySettings
import prince.sonic.music.ui.screens.settings.RomanizationSettings
import prince.sonic.music.ui.screens.settings.SettingsScreen
import prince.sonic.music.ui.screens.settings.StorageSettings
import prince.sonic.music.ui.screens.settings.SupporterScreen
import prince.sonic.music.ui.screens.settings.SupporterScreen
import prince.sonic.music.ui.screens.settings.SupporterScreen
import prince.sonic.music.ui.screens.settings.UpdaterScreen
import prince.sonic.music.ui.screens.settings.AiSettings
import prince.sonic.music.ui.screens.settings.DiscordLoginScreen
import prince.sonic.music.ui.screens.settings.DiagnosticsSettings
import prince.sonic.music.ui.screens.settings.DiscordSettings
import prince.sonic.music.ui.screens.settings.LastFMSettings
import prince.sonic.music.ui.screens.settings.NetworkTroubleshootSettings
import prince.sonic.music.ui.utils.ShowMediaInfo
import prince.sonic.music.ui.player.VideoPlayerScreen
import prince.sonic.music.utils.rememberEnumPreference
import prince.sonic.music.utils.rememberPreference

private const val TOP_LEVEL_TAB_ANIMATION_DURATION = 340
private const val TOP_LEVEL_TAB_FADE_IN_DURATION = 260
private const val TOP_LEVEL_TAB_FADE_OUT_DURATION = 240

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelTabEnterTransition() =
    when (targetState.destination.route) {
        Screens.Library.route ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))

        Screens.Home.route ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))

        else -> fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))
    }

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelTabExitTransition() =
    when (targetState.destination.route) {
        Screens.Library.route ->
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))

        Screens.Home.route ->
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))

        else -> fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))
    }

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelTabPopEnterTransition() =
    when (initialState.destination.route) {
        Screens.Library.route ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))

        Screens.Home.route ->
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))

        else -> fadeIn(tween(TOP_LEVEL_TAB_FADE_IN_DURATION, easing = FastOutSlowInEasing))
    }

private fun AnimatedContentTransitionScope<NavBackStackEntry>.topLevelTabPopExitTransition() =
    when (initialState.destination.route) {
        Screens.Library.route ->
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))

        Screens.Home.route ->
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                tween(TOP_LEVEL_TAB_ANIMATION_DURATION, easing = FastOutSlowInEasing),
            ) + fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))

        else -> fadeOut(tween(TOP_LEVEL_TAB_FADE_OUT_DURATION, easing = FastOutSlowInEasing))
    }


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.navigationBuilder(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    latestVersionName: String,
    onOpenPlayer: () -> Unit,
) {
    composable(
        Screens.Home.route,
        enterTransition = { topLevelTabEnterTransition() },
        exitTransition = { topLevelTabExitTransition() },
        popEnterTransition = { topLevelTabPopEnterTransition() },
        popExitTransition = { topLevelTabPopExitTransition() },
    ) {
        HomeScreen(navController)
    }
    composable(Screens.Search.route) {
        SearchScreen(navController, onSearchBarClick = { /* Search bar opens automatically via active state */ })
    }
    composable(Screens.Find.route) {
        FindSongScreen(navController, onOpenPlayer = onOpenPlayer)
    }
    composable(
        Screens.Library.route,
        enterTransition = { topLevelTabEnterTransition() },
        exitTransition = { topLevelTabExitTransition() },
        popEnterTransition = { topLevelTabPopEnterTransition() },
        popExitTransition = { topLevelTabPopExitTransition() },
    ) {
        LibraryScreen(navController)
    }
    composable("history") {
        HistoryScreen(navController)
    }
    composable("local_media") {
        prince.sonic.music.ui.screens.library.LocalMediaScreen(navController)
    }
    composable("stats") {
        StatsScreen(navController)
    }
    composable("spotify_import") {
        SpotifyImportScreen(navController)
    }
    composable("mood_and_genres") {
        MoodAndGenresScreen(navController, scrollBehavior)
    }
    composable("account") {
        AccountScreen(navController, scrollBehavior)
    }
    composable("new_release") {
        NewReleaseScreen(navController, scrollBehavior)
    }
    composable("charts_screen") {
       ChartsScreen(navController)
    }
    composable(
        route = "browse/{browseId}",
        arguments = listOf(
            navArgument("browseId") {
                type = NavType.StringType
            }
        )
    ) {
        BrowseScreen(
            navController,
            scrollBehavior,
            it.arguments?.getString("browseId")
        )
    }
    composable(
        route = "search/{query}?autoplay={autoplay}",
        arguments =
        listOf(
            navArgument("query") {
                type = NavType.StringType
            },
            navArgument("autoplay") {
                type = NavType.BoolType
                defaultValue = false
            },
        ),
        enterTransition = {
            fadeIn(tween(250))
        },
        exitTransition = {
            if (targetState.destination.route?.startsWith("search/") == true) {
                fadeOut(tween(200))
            } else {
                fadeOut(tween(200)) + slideOutHorizontally { -it / 2 }
            }
        },
        popEnterTransition = {
            if (initialState.destination.route?.startsWith("search/") == true) {
                fadeIn(tween(250))
            } else {
                fadeIn(tween(250)) + slideInHorizontally { -it / 2 }
            }
        },
        popExitTransition = {
            fadeOut(tween(200))
        },
    ) {
        OnlineSearchResult(navController)
    }
    composable(
        route = "album/{albumId}",
        arguments =
        listOf(
            navArgument("albumId") {
                type = NavType.StringType
            },
        ),
    ) {
        AlbumScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}",
        arguments =
        listOf(
            navArgument("artistId") {
                type = NavType.StringType
            },
        ),
    ) {
        ArtistScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/songs",
        arguments =
        listOf(
            navArgument("artistId") {
                type = NavType.StringType
            },
        ),
    ) {
        ArtistSongsScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/albums",
        arguments = listOf(
            navArgument("artistId") {
                type = NavType.StringType
            }
        )
    ) {
        ArtistAlbumsScreen(navController, scrollBehavior)
    }
    composable(
        route = "artist/{artistId}/items?browseId={browseId}?params={params}",
        arguments =
        listOf(
            navArgument("artistId") {
                type = NavType.StringType
            },
            navArgument("browseId") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("params") {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        ArtistItemsScreen(navController, scrollBehavior)
    }
    composable(
        route = "online_playlist/{playlistId}",
        arguments =
        listOf(
            navArgument("playlistId") {
                type = NavType.StringType
            },
        ),
    ) {
        OnlinePlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "podcast/{podcastId}",
        arguments =
        listOf(
            navArgument("podcastId") {
                type = NavType.StringType
            },
        ),
    ) {
        OnlinePodcastScreen(navController, scrollBehavior)
    }
    composable(
        route = "local_playlist/{playlistId}",
        arguments =
        listOf(
            navArgument("playlistId") {
                type = NavType.StringType
            },
        ),
    ) {
        LocalPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "auto_playlist/{playlist}",
        arguments =
        listOf(
            navArgument("playlist") {
                type = NavType.StringType
            },
        ),
    ) {
        AutoPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "cache_playlist/{playlist}",
        arguments =
            listOf(
                navArgument("playlist") {
                    type = NavType.StringType
            },
        ),
    ) {
        CachePlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "top_playlist/{top}",
        arguments =
        listOf(
            navArgument("top") {
                type = NavType.StringType
            },
        ),
    ) {
        TopPlaylistScreen(navController, scrollBehavior)
    }
    composable(
        route = "youtube_browse/{browseId}?params={params}",
        arguments =
        listOf(
            navArgument("browseId") {
                type = NavType.StringType
                nullable = true
            },
            navArgument("params") {
                type = NavType.StringType
                nullable = true
            },
        ),
    ) {
        YouTubeBrowseScreen(navController)
    }
    composable("settings") {
        SettingsScreen(navController, scrollBehavior, latestVersionName)
    }
    composable("wrapped") {
        WrappedScreen(navController)
    }
    composable("settings/appearance") {
        AppearanceSettings(navController, scrollBehavior)
    }
    composable("settings/content") {
        ContentSettings(navController, scrollBehavior)
    }
    composable("settings/content/po_token") {
        PoTokenScreen(navController)
    }
    composable("settings/content/romanization") {
        RomanizationSettings(navController, scrollBehavior)
    }
    composable("settings/player") {
        PlayerSettings(navController, scrollBehavior)
    }
    composable("settings/storage") {
        StorageSettings(navController, scrollBehavior)
    }
    composable("settings/privacy") {
        PrivacySettings(navController, scrollBehavior)
    }
    composable("settings/diagnostics") {
        DiagnosticsSettings(navController, scrollBehavior)
    }
    composable("settings/network_troubleshoot") {
        NetworkTroubleshootSettings(navController, scrollBehavior)
    }
    composable("settings/backup_restore") {
        BackupAndRestore(navController, scrollBehavior)
    }
    composable("settings/updater") {
        UpdaterScreen(navController, scrollBehavior)
    }
    composable("settings/about") {
        AboutScreen(navController, scrollBehavior)
    }
    composable("settings/supporter") {
        SupporterScreen(navController, scrollBehavior)
    }
    composable("settings/ai") {
        AiSettings(navController, scrollBehavior)
    }
    composable("settings/discord") {
        DiscordSettings(navController, scrollBehavior)
    }
    composable("settings/discord/login") {
        DiscordLoginScreen(navController)
    }
    composable("settings/lastfm") {
        LastFMSettings(navController, scrollBehavior)
    }
    composable("listen_together") {
        ListenTogetherScreen(navController)
    }
    composable("login") {
        LoginScreen(navController)
    }
    composable(
        route = "video/{videoId}",
        arguments = listOf(
            navArgument("videoId") {
                type = NavType.StringType
            }
        )
    ) {
        VideoPlayerScreen(
            videoId = it.arguments?.getString("videoId") ?: "",
            navController = navController
        )
    }
    composable("ambient_mode") {
        AmbientModeScreen(navController)
    }
}
