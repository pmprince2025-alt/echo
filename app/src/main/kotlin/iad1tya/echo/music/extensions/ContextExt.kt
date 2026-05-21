package prince.sonic.music.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import prince.sonic.music.constants.InnerTubeCookieKey
import prince.sonic.music.constants.YtmSyncKey
import prince.sonic.music.utils.dataStore
import prince.sonic.music.utils.get
import com.echo.innertube.utils.parseCookieString
import kotlinx.coroutines.runBlocking

fun Context.isSyncEnabled(): Boolean {
    return runBlocking {
        dataStore.get(YtmSyncKey, true) && isUserLoggedIn()
    }
}

fun Context.isUserLoggedIn(): Boolean {
    return runBlocking {
        val cookie = dataStore[InnerTubeCookieKey] ?: ""
        "SAPISID" in parseCookieString(cookie) && isInternetConnected()
    }
}

fun Context.isInternetConnected(): Boolean {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
}
