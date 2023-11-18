package info.jukov.rijksmuseum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import info.jukov.rijksmuseum.feature.art.item.presentation.ArtItemScreen
import info.jukov.rijksmuseum.feature.artcollection.presentation.ArtCollectionScreen
import info.jukov.rijksmuseum.ui.theme.RijksmuseumTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            RijksmuseumTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NavHost(navController = navController, startDestination = "art/collection") {
                        composable("art/collection") {
                            ArtCollectionScreen(
                                onItemClick = { itemId: String ->
                                    navController.navigate("art/$itemId")
                                }
                            )
                        }
                        composable(
                            "art/{itemId}",
                            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            ArtItemScreen(
                                itemId = requireNotNull(backStackEntry.arguments?.getString("itemId"))
                            )
                        }
                    }
                }
            }
        }
    }
}