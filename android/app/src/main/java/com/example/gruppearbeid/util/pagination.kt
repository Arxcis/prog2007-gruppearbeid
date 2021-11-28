package com.example.gruppearbeid.util

import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.gruppearbeid.types.Results

fun <Thing>refreshPaginationViews(res: Results<Thing>, PrevView: View, NextView: View, DotsView: TextView) {
    when {
        res.prev != null -> PrevView.visibility = View.VISIBLE
        else -> PrevView.visibility = View.INVISIBLE
    }
    when {
        res.next != null -> NextView.visibility = View.VISIBLE
        else -> NextView.visibility = View.INVISIBLE
    }

    var dots = ""
    for (i in (1..res.pageCount)) {
        dots = when (i){
            res.page -> dots.plus(Constants.DOT_BIG).plus(" ")
            else -> dots.plus(Constants.DOT_SMALL).plus(" ")
        }
    }
    DotsView.text = dots
}
