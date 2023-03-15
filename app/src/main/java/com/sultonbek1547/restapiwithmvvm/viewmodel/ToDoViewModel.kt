package com.sultonbek1547.restapiwithmvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sultonbek1547.restapiwithmvvm.model.MyToDo
import com.sultonbek1547.restapiwithmvvm.model.MyToDoRequest
import com.sultonbek1547.restapiwithmvvm.repository.ToDoRepository
import com.sultonbek1547.restapiwithmvvm.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ToDoViewModel(val toDoRepository: ToDoRepository) : ViewModel() {
    private val liveData = MutableLiveData<Resource<List<MyToDo>>>()

    fun getAllToDo(): MutableLiveData<Resource<List<MyToDo>>> {
        viewModelScope.launch {
            liveData.postValue(Resource.loading("loading"))

            try {
                coroutineScope {
                    val list = async {
                        toDoRepository.getAllToDo()
                    }.await()
                    liveData.postValue(Resource.success(list))
                }

            } catch (e: Exception) {
                liveData.postValue(Resource.error(e.message))
            }

        }
        return liveData
    }

    private val postLiveData = MutableLiveData<Resource<MyToDo>>()
    fun addMyToDo(myToDoRequest: MyToDoRequest): MutableLiveData<Resource<MyToDo>> {
        viewModelScope.launch {
            postLiveData.postValue(Resource.loading("loading"))
            try {

                coroutineScope {
                    val response = async { toDoRepository.addToDo(myToDoRequest) }.await()
                    postLiveData.postValue(Resource.success(response))
                }

            } catch (e: java.lang.Exception) {
                liveData.postValue(Resource.error(e.message))

            }

        }

        return postLiveData
    }

    private val liveDataUpdate = MutableLiveData<Resource<MyToDo>>()
    fun updateMyToDo(id: Int, myToDoRequest: MyToDoRequest): MutableLiveData<Resource<MyToDo>> {

        viewModelScope.launch {
            liveDataUpdate.postValue(Resource.loading("loading update"))
            try {

                coroutineScope {
                    val response = async { toDoRepository.updateToDo(id, myToDoRequest) }.await()
                    liveDataUpdate.postValue(Resource.success(response))
                }

            } catch (e: java.lang.Exception) {
                liveDataUpdate.postValue(Resource.error(e.message))

            }

        }

        return liveDataUpdate
    }

   private val deleteLiveData = MutableLiveData<Resource<ResponseBody>>()
    fun deleteToDo(id: Int): MutableLiveData<Resource<ResponseBody>> {
        viewModelScope.launch {
            deleteLiveData.postValue(Resource.loading("loading delete"))
            try {

                coroutineScope {
                    val response = async { toDoRepository.deleteToDo(id) }.await()
                    deleteLiveData.postValue(Resource.successDelete(response.toString()))
                }

            } catch (e: java.lang.Exception) {
                deleteLiveData.postValue(Resource.error(e.message))

            }

        }

        return deleteLiveData
    }

}