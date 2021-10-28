package com.example.gruppearbeid.util

import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gruppearbeid.*
import com.google.android.material.bottomnavigation.BottomNavigationView


fun configureBottomNavigation(from: AppCompatActivity, Nav: BottomNavigationView, selectedItemId: Int) {

    Nav.selectedItemId = selectedItemId

    Nav.setOnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.PeopleMenuItem -> startActivityWithIntent(from, PeopleActivity::class.java)
            R.id.PlanetsMenuItem -> startActivityWithIntent(from, PlanetsActivity::class.java)
            R.id.FilmsMenuItem -> startActivityWithIntent(from, FilmsActivity::class.java)
            R.id.SpaceshipsMenuItem -> startActivityWithIntent(from, SpaceshipsActivity::class.java)
            else -> logAndReturnFalse(from, item.itemId)
        }
    }
}

private fun <T>startActivityWithIntent(from: AppCompatActivity, to: Class<T>): Boolean {
    val intent = Intent(from, to)
    from.startActivity(intent)
    Log.i("startActivityWithIntent","Navigating from ${from::class.simpleName} to ${to::class.simpleName}")

    return true
}

private fun logAndReturnFalse(from: AppCompatActivity, itemId: Int): Boolean {
    Log.w("logAndReturnFalse", "activity: ${from::class.simpleName}: unknown item.itemId: $itemId")
    return false
}

