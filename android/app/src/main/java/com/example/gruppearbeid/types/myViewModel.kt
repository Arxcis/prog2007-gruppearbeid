package com.example.gruppearbeid.types

import android.content.ClipData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class myViewModel : ViewModel() {
    private var mutableItem = MutableLiveData<String>()
    val item: LiveData<String> get() = mutableItem

    fun selectItem(str: String)
    {
        mutableItem.value = str
    }
}