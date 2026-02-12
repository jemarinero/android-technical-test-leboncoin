package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.adevinta.spark.SparkTheme

class MainActivity : ComponentActivity() {

//    private val viewModel: AlbumsViewModel by lazy {
//        val dependencies = (application as AppDependenciesProvider).dependencies
//        val factory = AlbumsViewModel.Factory(dependencies.dataDependencies.albumsRepository)
//        ViewModelProvider(this, factory)[AlbumsViewModel::class.java]
//    }
//
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
//                AlbumsScreen(
//                    viewModel = viewModel,
//                    onItemSelected = {
//                        analyticsHelper.trackSelection(it.id.toString())
//                        startActivity(Intent(this, DetailsActivity::class.java))
//                    }
//                )
            }
        }
    }
}