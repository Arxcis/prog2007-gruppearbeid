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
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_people.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import com.example.gruppearbeid.util.navigateToThing

class PeopleActivity : AppCompatActivity() {//This activity has been set as the first one which starts when opening up
//the app.
    private val people = ArrayList<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)
        title = "People"
        val connectionMng: ConnectivityManager? = ContextCompat.getSystemService(this, ConnectivityManager::class.java)
        if (connectionMng !== null)
        {
            Network.connectionMng = connectionMng
            Network.checkInternetConnection()
        }
        // Init adapter
        val adapter = PeopleAdapter(people){ person ->
            navigateToThing(this, PersonActivity::class.java, person)
        }
        Network.getPeople(people, adapter){ error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }

        PeopleRecycler.adapter = adapter
        PeopleRecycler.layoutManager = LinearLayoutManager(this)
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}
