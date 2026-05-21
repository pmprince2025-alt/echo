package iad1tya.echo.music.innertube.models.body

import iad1tya.echo.music.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class GetSearchSuggestionsBody(
    val context: Context,
    val input: String,
)

@Serializable
data class GetQueueBody(
    val context: Context,
    val videoIds: List<String>? = null,
    val playlistId: String? = null,
)

@Serializable
data class GetTranscriptBody(
    val context: Context,
    val params: String,
)

@Serializable
data class AccountMenuBody(
    val context: Context,
)

@Serializable
data class LikeBody(
    val context: Context,
    val target: Target,
) {
    @Serializable
    sealed class Target {
        @Serializable
        data class VideoTarget(val videoId: String) : Target()

        @Serializable
        data class PlaylistTarget(val playlistId: String) : Target()
    }
}

@Serializable
data class SubscribeBody(
    val context: Context,
    val channelIds: List<String>,
)

@Serializable
data class CreatePlaylistBody(
    val context: Context,
    val title: String,
)

@Serializable
data class PlaylistDeleteBody(
    val context: Context,
    val playlistId: String,
)
