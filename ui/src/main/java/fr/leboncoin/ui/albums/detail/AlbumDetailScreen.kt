package fr.leboncoin.ui.albums.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import fr.leboncoin.domain.models.AlbumModel
import fr.leboncoin.ui.R
import fr.leboncoin.ui.components.FullscreenEmpty
import fr.leboncoin.ui.components.FullscreenError
import fr.leboncoin.ui.components.FullscreenLoading

@Composable
fun AlbumDetailScreen(
    viewModel: AlbumDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            AlbumDetailToolbar(
                album = state.album,
                onBackClick = onBackClick
            )
        }

    ) { padding ->
        AlbumDetailContent(
            state = state,
            modifier = Modifier.padding(padding)
        ) {
            viewModel.onErrorClose()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDetailToolbar(
    album: AlbumModel?,
    onBackClick: () -> Unit
) {

    val title = album?.let {
        stringResource(
            R.string.toolbar_title_album_detail,
            it.albumId,
            it.id
        )
    } ?: ""

    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        }
    )
}


@Composable
fun AlbumDetailContent(
    state: AlbumDetailUiState,
    modifier: Modifier = Modifier,
    onErrorClose: () -> Unit = {}
) {

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        when {
            state.isLoading -> FullscreenLoading()
            state.error != null ->
                FullscreenError(state.error) {
                    onErrorClose.invoke()
                }

            state.album != null -> AlbumDetail(state.album)
            else -> FullscreenEmpty(stringResource(R.string.empty_no_data))
        }
    }
}

@Composable
fun AlbumDetail(
    album: AlbumModel
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        AsyncImage(
            model = album.url,
            contentDescription = album.title,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = album.title,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}


