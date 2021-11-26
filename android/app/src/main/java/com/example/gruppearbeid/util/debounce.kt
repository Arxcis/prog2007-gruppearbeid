package com.example.gruppearbeid.util

import android.text.Editable
import android.text.TextWatcher
import java.util.*

/**
 * Code adopted from https://stackoverflow.com/a/12143050 2021-11-26
 */
fun makeTextWatcherWithDebounce(DELAY: Long, run: (text: String) -> Unit): TextWatcher {
    return object : TextWatcher {
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun beforeTextChanged(
            s: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        private var timer = Timer()
        override fun afterTextChanged(s: Editable) {
            timer.cancel()
            timer = Timer()
            timer.schedule(
                object : TimerTask() {
                    override fun run() = run(s.toString())
                },
                DELAY
            )
        }
    }
}