package iad1tya.echo.music.innertube.models

data class MediaInfo(
    val videoId: String,
    val title: String? = null,
    val author: String? = null,
    val authorId: String? = null,
    val authorThumbnail: String? = null,
    val description: String? = null,
    val subscribers: String? = null,
    val uploadDate: String? = null,
    val viewCount: Long? = null,
    val like: Long? = null,
    val dislike: Long? = null,
)
