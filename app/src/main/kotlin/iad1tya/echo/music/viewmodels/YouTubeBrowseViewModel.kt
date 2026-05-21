package prince.sonic.music.viewmodels

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.echo.innertube.YouTube
import com.echo.innertube.pages.BrowseResult
import prince.sonic.music.constants.HideExplicitKey
import prince.sonic.music.constants.HideVideoSongsKey
import prince.sonic.music.constants.HideYoutubeShortsKey
import prince.sonic.music.utils.dataStore
import prince.sonic.music.utils.get
import prince.sonic.music.utils.reportException
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class YouTubeBrowseViewModel
@Inject
constructor(
    @ApplicationContext val context: Context,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val browseId = savedStateHandle.get<String>("browseId")!!
    private val params = savedStateHandle.get<String>("params")

    val result = MutableStateFlow<BrowseResult?>(null)

    init {
        viewModelScope.launch {
            YouTube
                .browse(browseId, params)
                .onSuccess {
                    result.value = it
                        .filterExplicit(context.dataStore.get(HideExplicitKey, false))
                        .filterVideoSongs(context.dataStore.get(HideVideoSongsKey, false))
                        .filterYoutubeShorts(context.dataStore.get(HideYoutubeShortsKey, false))
                }.onFailure {
                    reportException(it)
                }
        }
    }
}
