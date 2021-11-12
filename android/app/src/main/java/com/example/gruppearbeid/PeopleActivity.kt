package com.example.gruppearbeid

// Third party
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gruppearbeid.adapters.FilmsAdapter
import com.example.gruppearbeid.adapters.PeopleAdapter
import com.example.gruppearbeid.util.Network
import kotlinx.android.synthetic.main.activity_people.*

// Local
import com.example.gruppearbeid.util.configureBottomNavigation
import kotlinx.android.synthetic.main.activity_films.*


class PeopleActivity : AppCompatActivity() { //This activity has been set as the first one which starts when opening up
                                             //the app.


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_people)

        val connectionMng: ConnectivityManager? = ContextCompat.getSystemService(this, ConnectivityManager::class.java)
        if (connectionMng !== null)
        {
            Network.connectionMng = connectionMng
            Network.checkInternetConnection()
        }
        if(com.example.gruppearbeid.util.Network.lostNetwork == false) {
            /*I think this if branch always runs because the ConnectivityManager's thread which operates the
                callback for notifying about network changes doesn't have the time to notify about no internet connection
                before the People acitivity updates the UI and tries to fetch data from API.
                    */

            // Init adapter
            val adapter = PeopleAdapter()
            PeopleRecycler.adapter = adapter
            PeopleRecycler.layoutManager = LinearLayoutManager(this)
        }
    }
    override fun onResume() {
        super.onResume()
        configureBottomNavigation(this, PeopleNavigation, R.id.PeopleMenuItem)
    }
}
