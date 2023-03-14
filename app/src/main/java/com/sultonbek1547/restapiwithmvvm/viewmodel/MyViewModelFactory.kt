package com.sultonbek1547.restapiwithmvvm.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sultonbek1547.restapiwithmvvm.repository.ToDoRepository

class MyViewModelFactory(private val toDoRepository: ToDoRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ToDoViewModel::class.java)){
            return ToDoViewModel(toDoRepository)as T
        }
        throw java.lang.IllegalArgumentException("Error")

        return super.create(modelClass)
    }
}