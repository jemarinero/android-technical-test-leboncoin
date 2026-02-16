package fr.leboncoin.ui.extensions

import android.content.Context
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.CachePolicy
import coil3.request.ImageRequest
import coil3.request.allowHardware
import coil3.request.crossfade
import coil3.request.error
import coil3.request.fallback
import coil3.request.placeholder
import fr.leboncoin.ui.R

fun Context.buildAlbumImageRequest(url: String): ImageRequest {
    return ImageRequest.Builder(this)
        .data(url)
        .httpHeaders(
            NetworkHeaders.Builder()
                .add("User-Agent", "LeboncoinApp/1.0")
                .build()
        )
        .placeholder(R.drawable.ic_image_placeholder)
        .error(R.drawable.ic_image_placeholder)
        .fallback(R.drawable.ic_image_placeholder)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .crossfade(true)
        .allowHardware(true)
        .build()
}