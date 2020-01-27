package org.shirshov.testtask.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.shirshov.testtask.databinding.MainActivityBinding
import org.shirshov.testtask.ui.fragment.album.SearchFragment
import org.shirshov.testtask.ui.navigation.Navigate

// Always refer to current instance of single activity, so no leaks here
lateinit var mainActivity: MainActivity
    private set

class MainActivity : AppCompatActivity() {

    lateinit var b: MainActivityBinding

    init {
        mainActivity = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = MainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        if (savedInstanceState == null) {
            Navigate.to(SearchFragment(), null)
        }
    }

}
