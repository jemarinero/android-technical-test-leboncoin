package fr.leboncoin.domain.repositories

interface AnalyticsRepository {
    fun trackSelection(itemId: String)
    fun trackScreenView(screenName: String)
}