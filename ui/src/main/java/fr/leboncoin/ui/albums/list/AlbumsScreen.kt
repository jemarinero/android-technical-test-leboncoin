package fr.leboncoin.ui.albums.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumsViewModel = hiltViewModel(),
    onItemClick: (AlbumModel) -> Unit,
) {

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var menuExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        topBar = {

            TopAppBar(
                title = { Text(stringResource(R.string.toolbar_title_albums)) },
                actions = {
                    IconButton(
                        onClick = { menuExpanded = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = stringResource(R.string.menu_force_refresh_description)
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.menu_force_refresh)) },
                            onClick = {
                                menuExpanded = false
                                viewModel.refresh()
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

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
                    contentPadding = paddingValues,
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
