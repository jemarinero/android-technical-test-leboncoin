package fr.leboncoin.ui.albums.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.adevinta.spark.components.scaffold.Scaffold
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.ui.R
import fr.leboncoin.ui.components.FullscreenEmpty
import fr.leboncoin.ui.components.FullscreenError
import fr.leboncoin.ui.components.FullscreenLoading

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = hiltViewModel(),
    onItemClick: (AlbumModel) -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(modifier = modifier) {
        when {
            state.isLoading -> FullscreenLoading()
            state.error != null -> state.error?.let { errorUiModel ->
                FullscreenError(errorUiModel) {
                    viewModel.onErrorClose()
                }
            } ?: FullscreenEmpty(stringResource(R.string.empty_no_data))

            state.albums.isEmpty() -> FullscreenEmpty(stringResource(R.string.empty_no_data))
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = it,
                ) {
                    items(
                        items = state.albums,
                        key = { album -> album.id }
                    ) { album ->
                        AlbumItem(
                            album = album,
                            onItemSelected = onItemClick,
                        )
                    }
                }
            }
        }
    }
}