package fr.leboncoin.ui.navigation

object Routes {
    const val LIST = "list"

    const val DETAIL = "detail/{$ARG_ID}/{$ARG_ALBUM_ID}"

    fun detail(id: Int, albumId: Int): String {
        return "detail/$id/$albumId"
    }
}
