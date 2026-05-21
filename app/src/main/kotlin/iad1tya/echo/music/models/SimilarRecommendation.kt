package prince.sonic.music.models

import com.echo.innertube.models.YTItem
import prince.sonic.music.db.entities.LocalItem

data class SimilarRecommendation(
    val title: LocalItem,
    val items: List<YTItem>,
)
