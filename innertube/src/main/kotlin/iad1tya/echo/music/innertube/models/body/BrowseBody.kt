package iad1tya.echo.music.innertube.models.body

import iad1tya.echo.music.innertube.models.Context
import kotlinx.serialization.Serializable

@Serializable
data class BrowseBody(
    val context: Context,
    val browseId: String? = null,
    val params: String? = null,
    val continuation: String? = null,
)
