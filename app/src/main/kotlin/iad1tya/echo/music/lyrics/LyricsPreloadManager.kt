package prince.sonic.music.lyrics

import android.content.Context
import android.util.Log
import prince.sonic.music.constants.PreloadQueueLyricsEnabledKey
import prince.sonic.music.constants.QueueLyricsPreloadCountKey
import prince.sonic.music.db.MusicDatabase
import prince.sonic.music.db.entities.LyricsEntity
import prince.sonic.music.models.MediaMetadata
import prince.sonic.music.utils.NetworkConnectivityObserver
import prince.sonic.music.utils.dataStore
import prince.sonic.music.utils.reportException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LyricsPreloadManager(
    private val context: Context,
    private val database: MusicDatabase,
    private val networkConnectivity: NetworkConnectivityObserver,
    private val lyricsHelper: LyricsHelper,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var preloadJob: Job? = null

    fun onSongChanged(currentIndex: Int, queue: List<MediaMetadata>) {
        preloadJob?.cancel()

        scope.launch {
            try {
                val preferences = context.dataStore.data.first()
                val isEnabled = preferences[PreloadQueueLyricsEnabledKey] ?: true
                if (!isEnabled) {
                    Log.d(TAG, "Queue lyrics preload is disabled")
                    return@launch
                }

                val isNetworkAvailable = try {
                    networkConnectivity.isCurrentlyConnected()
                } catch (_: Exception) {
                    true
                }

                if (!isNetworkAvailable) {
                    Log.w(TAG, "Network unavailable, skipping lyrics preload")
                    return@launch
                }

                val preloadCount = preferences[QueueLyricsPreloadCountKey] ?: DEFAULT_PRELOAD_COUNT
                val nextSongs = getNextSongs(queue, currentIndex, preloadCount)
                if (nextSongs.isEmpty()) return@launch

                preloadLyrics(nextSongs)
            } catch (e: Exception) {
                reportException(e)
            }
        }
    }

    private fun getNextSongs(queue: List<MediaMetadata>, currentIndex: Int, count: Int): List<MediaMetadata> {
        if (queue.isEmpty() || currentIndex < 0 || count <= 0) return emptyList()

        val startIndex = currentIndex + 1
        if (startIndex >= queue.size) return emptyList()

        val endIndex = minOf(startIndex + count, queue.size)
        return queue.subList(startIndex, endIndex)
    }

    private fun preloadLyrics(songs: List<MediaMetadata>) {
        preloadJob = scope.launch {
            try {
                songs.forEach { song ->
                    val existingLyrics = database.lyrics(song.id).first()
                    if (existingLyrics != null && existingLyrics.lyrics != LyricsEntity.LYRICS_NOT_FOUND) {
                        return@forEach
                    }

                    runCatching {
                        lyricsHelper.getLyricsWithProvider(song)
                    }.onSuccess { lyrics ->
                        if (!lyrics.lyrics.isNullOrBlank() && lyrics.lyrics != LyricsEntity.LYRICS_NOT_FOUND) {
                            database.query {
                                upsert(LyricsEntity(id = song.id, lyrics = lyrics.lyrics, provider = lyrics.providerName))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                reportException(e)
            }
        }
    }

    fun destroy() {
        preloadJob?.cancel()
        preloadJob = null
        scope.cancel()
    }

    private companion object {
        private const val TAG = "LyricsPreloadManager"
        private const val DEFAULT_PRELOAD_COUNT = 3
    }
}
