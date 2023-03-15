package com.sultonbek1547.restapiwithmvvm.retrofit

import com.sultonbek1547.restapiwithmvvm.model.MyToDo
import com.sultonbek1547.restapiwithmvvm.model.MyToDoRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @GET("plan")
    suspend fun getAllToDo(): List<MyToDo>

    @POST("plan/")
    suspend fun addToDo(@Body myToDoRequest: MyToDoRequest): MyToDo

    @PUT("plan/{id}/")
    suspend fun updateToDo(@Path("id") id: Int, @Body myToDoRequest: MyToDoRequest): MyToDo

    @DELETE("plan/{id}/")
    suspend fun deleteToDo(@Path("id") id: Int): Response<Int>?


}