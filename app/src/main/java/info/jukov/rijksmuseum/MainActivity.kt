package info.jukov.rijksmuseum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import info.jukov.rijksmuseum.feature.art.collection.presentation.ArtCollectionScreen
import info.jukov.rijksmuseum.feature.art.details.presentation.ArtDetailsScreen
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Rijksmuseum)

        setContent {
            val navController = rememberNavController()

            RijksmuseumTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "art/collection") {
                        composable("art/collection") {
                            ArtCollectionScreen(
                                onItemClick = { itemId: String, itemName: String ->
                                    navController.navigate(
                                        "art/$itemId/$itemName",

                                    )
                                }
                            )
                        }
                        composable(
                            "art/{itemId}/{itemName}",
                            arguments = listOf(
                                navArgument(Const.Keys.ITEM_ID) { type = NavType.StringType },
                                navArgument(Const.Keys.ITEM_NAME) { type = NavType.StringType },
                            )
                        ) { backStackEntry ->
                            ArtDetailsScreen(
                                onBackClick = { navController.popBackStack() },
                                itemName = requireNotNull(backStackEntry.arguments?.getString(Const.Keys.ITEM_NAME))
                            )
                        }
                    }
                }
            }
        }
    }
}