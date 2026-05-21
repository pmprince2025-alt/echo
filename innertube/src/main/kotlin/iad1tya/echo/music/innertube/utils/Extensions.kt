package iad1tya.echo.music.innertube.utils

import java.security.MessageDigest

fun sha1(input: String): String {
    val bytes = MessageDigest.getInstance("SHA-1").digest(input.encodeToByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}
