package fr.leboncoin.ui.models

import fr.leboncoin.domain.models.ErrorType
import fr.leboncoin.ui.R

data class ErrorUiModel(
    val title: Int,
    val description: Int
)

fun ErrorType.toUiModel(): ErrorUiModel {
    return when (this) {
        ErrorType.UnknownError -> ErrorUiModel(
            title = R.string.error_unknown_title,
            description = R.string.error_unknown_description
        )

        ErrorType.ConnectivityError -> ErrorUiModel(
            title = R.string.error_connectivity_title,
            description = R.string.error_connectivity_description
        )

        ErrorType.ServerError -> ErrorUiModel(
            title = R.string.error_server_title,
            description = R.string.error_server_description
        )
    }
}
