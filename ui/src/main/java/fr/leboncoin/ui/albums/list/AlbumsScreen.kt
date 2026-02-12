package fr.leboncoin.ui.albums.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.adevinta.spark.components.scaffold.Scaffold

@Composable
fun AlbumsScreen(
    viewModel: AlbumsViewModel,
    onItemSelected : (/*AlbumDto*/) -> Unit,
    modifier: Modifier = Modifier,
) {
//    val albums by viewModel.albums.collectAsStateWithLifecycle(emptyList())

    //LaunchedEffect(Unit) { viewModel.loadAlbums() }

    Scaffold(modifier = modifier) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = it,
        ) {
//            items(
//                items = albums,
//                key = { album -> album.id }
//            ) { album ->
//                AlbumItem(
//                    album = album,
//                    onItemSelected = onItemSelected,
//                )
//            }
        }
    }
}