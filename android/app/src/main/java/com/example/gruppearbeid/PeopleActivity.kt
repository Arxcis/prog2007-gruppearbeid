package com.example.gruppearbeid

// Third party
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.PeopleAdapter
import android.widget.Toast
import com.example.gruppearbeid.types.Person
import com.example.gruppearbeid.types.currentActivity
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_people.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing

class PeopleActivity : AppCompatActivity() {//This activity has been set as the first one which starts when opening up
//the app.
    private val people = ArrayList<Person>()
    private lateinit var adapter: PeopleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        title = "People"
        // Init adapter
        adapter = PeopleAdapter(people){ person ->
            navigateToThing(this, PersonActivity::class.java, person)
        }
        Network.getPeople(people, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        PeopleRecycler.adapter = adapter
        PeopleRecycler.layoutManager = LinearLayoutManager(this)
    }

    fun fetchPeople()
    {
        people.clear()                               //prevent refresh button to display data twice.
        Network.getPeople(people, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        currentActivity.activity = title.toString()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }

    override fun onPause() {
        super.onPause()
        currentActivity.activity = null
    }

    override fun onDestroy() {
        super.onDestroy()
        currentActivity.activity = null
    }
}
