package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adevinta.spark.SparkTheme
import dagger.hilt.android.AndroidEntryPoint
import fr.leboncoin.ui.albums.list.AlbumsScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

//    private val analyticsHelper: AnalyticsHelper by lazy {
//        val dependencies = (application as AppDependenciesProvider).dependencies
//        dependencies.analyticsHelper
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        analyticsHelper.initialize(this)

        setContent {
            SparkTheme {
                //TODO change for navigation host
                AlbumsScreen(
                    onItemSelected = {
//                        analyticsHelper.trackSelection(it.id.toString())
//                        startActivity(Intent(this, DetailsActivity::class.java))
                    }
                )
            }
        }
    }
}