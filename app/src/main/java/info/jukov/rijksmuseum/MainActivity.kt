package info.jukov.rijksmuseum

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import info.jukov.rijksmuseum.ui.feature.list.presentation.CollectionFragment

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        savedInstanceState ?: showCollectionFragment()
    }

    private fun showCollectionFragment() {
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, CollectionFragment())
            .commit()
    }
}