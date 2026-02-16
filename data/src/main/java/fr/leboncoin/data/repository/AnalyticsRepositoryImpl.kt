package fr.leboncoin.data.repository

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.leboncoin.domain.repositories.AnalyticsRepository
import javax.inject.Inject

class AnalyticsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AnalyticsRepository {

    private val prefs by lazy {
        context.getSharedPreferences("analytics_prefs", Context.MODE_PRIVATE)
    }

    override fun trackSelection(itemId: String) {
        prefs.edit { putString("selected_item", itemId) }
        // Simulate some analytics logging
        println("Analytics: User selected item - $itemId")
    }

    override fun trackScreenView(screenName: String) {
        // Simulate some analytics logging
        println("Analytics: Screen viewed - $screenName")
    }
}
