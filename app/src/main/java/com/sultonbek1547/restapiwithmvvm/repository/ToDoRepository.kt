package com.sultonbek1547.restapiwithmvvm.repository

import com.sultonbek1547.restapiwithmvvm.model.MyToDoRequest
import com.sultonbek1547.restapiwithmvvm.retrofit.ApiService

class ToDoRepository(private val apiService: ApiService) {
    suspend fun getAllToDo() = apiService.getAllToDo()
    suspend fun addToDo(myToDoRequest: MyToDoRequest) = apiService.addToDo(myToDoRequest)
    suspend fun updateToDo(id: Int, myToDoRequest: MyToDoRequest) =
        apiService.updateToDo(id, myToDoRequest)

    suspend fun deleteToDo(id: Int) = apiService.deleteToDo(id)

}