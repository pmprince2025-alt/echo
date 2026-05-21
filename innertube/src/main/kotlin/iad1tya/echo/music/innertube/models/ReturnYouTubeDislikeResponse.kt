package iad1tya.echo.music.innertube.models

import kotlinx.serialization.Serializable

@Serializable
data class ReturnYouTubeDislikeResponse(
    val id: String,
    val dateCreated: String,
    val likes: Long,
    val dislikes: Long,
    val rating: Double,
    val viewCount: Long,
    val deleted: Boolean,
)
