package iad1tya.echo.music.innertube.utils

fun parseCookieString(cookie: String): Map<String, String> {
    return cookie.split(";")
        .map { it.trim() }
        .filter { it.contains("=") }
        .associate {
            val (key, value) = it.split("=", limit = 2)
            key.trim() to value.trim()
        }
}
