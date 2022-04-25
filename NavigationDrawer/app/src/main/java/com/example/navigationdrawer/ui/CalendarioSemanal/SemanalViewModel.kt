package com.example.navigationdrawer.ui.CalendarioSemanal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SemanalViewModel : ViewModel(){
    private val _text = MutableLiveData<String>().apply {
        value = "This is Semana Fragment"
    }
    val text: LiveData<String> = _text
}
