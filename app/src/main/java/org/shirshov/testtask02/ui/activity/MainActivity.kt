package org.shirshov.testtask02.ui.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.shirshov.testtask02.databinding.MainActivityBinding
import org.shirshov.testtask02.ui.fragment.sportevent.SportEventsFragment
import org.shirshov.testtask02.ui.navigation.Navigate

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
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        b = MainActivityBinding.inflate(layoutInflater)
        setContentView(b.root)
        setSupportActionBar(b.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        b.toolbar.setNavigationOnClickListener { onBackPressed() }
        Navigate.to(SportEventsFragment(), null)
    }

    // Restart from scratch when launched after process kill
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(Bundle())
    }

    fun showBack(show: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }
}
