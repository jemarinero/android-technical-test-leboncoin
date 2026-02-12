package fr.leboncoin.ui.extensions

import android.content.Context
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade

fun Context.buildAlbumImageRequest(url: String): ImageRequest {
    return ImageRequest.Builder(this)
        .data(url)
        .httpHeaders(
            NetworkHeaders.Builder()
                .add("User-Agent", "LeboncoinApp/1.0")
                .build()
        )
        .crossfade(true)
        .build()
}