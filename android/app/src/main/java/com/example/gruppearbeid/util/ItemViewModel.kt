package com.example.gruppearbeid.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ItemViewModel : ViewModel() {
    private val anItem = MutableLiveData<String>()
    val selectedItem: LiveData<String> get() = anItem

    fun selectItem(str: String)
    {
        anItem.value = str
    }

}