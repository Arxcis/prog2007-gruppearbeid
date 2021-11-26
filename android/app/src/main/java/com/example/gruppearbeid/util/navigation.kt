package com.example.gruppearbeid.util

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gruppearbeid.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.Serializable


fun configureBottomNavigation(from: AppCompatActivity, Nav: BottomNavigationView, selectedItemId: Int) {

    Nav.setOnItemSelectedListener(null)
    Nav.elevation = 0.0f;
    Nav.selectedItemId = selectedItemId
    Nav.setOnItemSelectedListener { item ->

        when (item.itemId) {
            R.id.PeopleMenuItem -> navigateToTab(from, PeopleActivity::class.java)
            R.id.PlanetsMenuItem -> navigateToTab(
                from,
                PlanetsActivity::class.java
            )
            R.id.FilmsMenuItem -> navigateToTab(from, FilmsActivity::class.java)
            R.id.StarshipsMenuItem -> navigateToTab(
                from,
                StarshipsActivity::class.java
            )
            R.id.SpeciesMenuItem -> navigateToTab(from, SpeciesListActivity::class.java)
            else -> logAndReturnFalse(from, item.itemId)
        }
    }
}

private fun <T>navigateToTab(from: AppCompatActivity, to: Class<T>): Boolean {
    val intent = Intent(from, to)
    from.startActivity(intent)

    // from.overridePendingTransition(0,0) turns off the navigation animation. This is desirable
    // when replacing an activity, because replaceActivity() is currently being used to navigate between tabs.
    from.overridePendingTransition(0,0)

    // Calling from.finish() removes the current activity from the stack,
    // effectively replacing the existing activity with the new one.
    // When the user clicks the back-button, he will not come back to this activity,
    // but may exit the app instead. This is the desired behaviour.
    from.finish()
    Log.i("navigateToTab","Navigating from ${from::class.simpleName} to ${to::class.simpleName}")

    return true
}

fun <T>navigateToThing(from: AppCompatActivity, to: Class<T>, thing: Serializable): Boolean {
    val intent = Intent(from, to).apply {
        putExtra(Constants.EXTRA_THING, thing)
    }
    from.startActivity(intent)
    Log.i("naviateToThing","Navigating from ${from::class.simpleName} to ${FilmActivity::class.simpleName}")
    return true
}

private fun logAndReturnFalse(from: AppCompatActivity, itemId: Int): Boolean {
    Log.w("logAndReturnFalse", "activity: ${from::class.simpleName}: unknown item.itemId: $itemId")
    return false
}

