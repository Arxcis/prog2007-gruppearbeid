package com.example.gruppearbeid

// Third party
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_people.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import kotlinx.android.synthetic.main.activity_films.*


class PeopleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}