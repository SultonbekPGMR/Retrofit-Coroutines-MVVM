package com.sultonbek1547.restapiwithmvvm.retrofit

import com.sultonbek1547.restapiwithmvvm.model.MyToDo
import com.sultonbek1547.restapiwithmvvm.model.MyToDoRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("plan")
    suspend fun getAllToDo(): List<MyToDo>

    @POST("plan/")
    suspend fun addToDo(@Body myToDoRequest: MyToDoRequest): MyToDo


}