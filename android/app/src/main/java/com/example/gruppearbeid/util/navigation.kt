package com.example.gruppearbeid.util

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gruppearbeid.*
import com.google.android.material.bottomnavigation.BottomNavigationView


fun configureBottomNavigation(from: AppCompatActivity, Nav: BottomNavigationView, selectedItemId: Int) {

    Nav.setOnItemSelectedListener(null)
    Nav.elevation = 0.0f;
    Nav.selectedItemId = selectedItemId
    Nav.setOnItemSelectedListener { item ->

        when (item.itemId) {
            R.id.PeopleMenuItem -> replaceActivity(from, PeopleActivity::class.java)
            R.id.PlanetsMenuItem -> replaceActivity(
                from,
                PlanetsActivity::class.java
            )
            R.id.FilmsMenuItem -> replaceActivity(from, FilmsActivity::class.java)
            R.id.StarshipsMenuItem -> replaceActivity(
                from,
                StarshipsActivity::class.java
            )
            else -> logAndReturnFalse(from, item.itemId)
        }
    }
}

private fun <T>replaceActivity(from: AppCompatActivity, to: Class<T>): Boolean {
    val intent = Intent(from, to)
    from.startActivity(intent)
    from.overridePendingTransition(0,0)
    // Calling .finish() removes the current activity from the stack,
    // effectively replacing the existing activity with the new one.
    // When the user clicks the back-button, he will not come back to this activity,
    // but may exit the app instead. This is the desired behaviour.
    from.finish()
    Log.i("startActivityWithIntent","Navigating from ${from::class.simpleName} to ${to::class.simpleName}")

    return true
}

private fun logAndReturnFalse(from: AppCompatActivity, itemId: Int): Boolean {
    Log.w("logAndReturnFalse", "activity: ${from::class.simpleName}: unknown item.itemId: $itemId")
    return false
}

