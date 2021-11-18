package com.example.gruppearbeid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gruppearbeid.types.Person
import kotlinx.android.synthetic.main.activity_film.*
import kotlinx.android.synthetic.main.activity_person.*

class PersonActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)

        // Get extras
        val person = intent.extras?.getSerializable("EXTRA_THING") as? Person

        // Set activity title
        title = person?.name

        // Set content of activity
        ActivityPersonName.text = person?.name ?: ""
    }
}