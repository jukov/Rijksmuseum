package info.jukov.rijksmuseum

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.espresso.Espresso
import androidx.test.espresso.NoActivityResumedException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun firstScreenIsArtCollection() {
        composeTestRule.apply {
            onNodeWithTag("ArtCollectionScaffold").assertIsDisplayed()
        }
    }

    @Test
    fun moveToArtDetails() {
        composeTestRule.apply {
            onNodeWithTag("card1").performClick()
            onNodeWithTag("ArtDetailsScaffold1").assertIsDisplayed()
        }
    }

    @Test
    fun moveToArtDetailsAndBack() {
        composeTestRule.apply {
            onNodeWithTag("card1").performClick()
            Espresso.pressBack()
            onNodeWithTag("ArtCollectionScaffold").assertIsDisplayed()
        }
    }

    @Test(expected = NoActivityResumedException::class)
    fun quitApp() {
        composeTestRule.apply {
            Espresso.pressBack()
        }
    }
}