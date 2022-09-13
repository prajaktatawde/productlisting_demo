package com.example.demolisting.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demolisting.adapter.RecyclerViewAdapter
import com.example.demolisting.models.RecyclerData
import com.example.demolisting.models.RecyclerList
import com.example.demolisting.network.RetroInstance
import com.example.demolisting.network.RetroService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivityViewModel : ViewModel() {

    var recyclerListLiveData: MutableLiveData<RecyclerList>
    var recyclerViewAdapter: RecyclerViewAdapter

    init {
        recyclerListLiveData = MutableLiveData()
        recyclerViewAdapter = RecyclerViewAdapter()
    }

    fun getAdapter(): RecyclerViewAdapter {
        return recyclerViewAdapter
    }

    fun setAdapterData(data: ArrayList<RecyclerData>) {
        recyclerViewAdapter.setDataList(data)
        recyclerViewAdapter.notifyDataSetChanged()
    }


    fun getRecyclerListObserver(): MutableLiveData<RecyclerList> {
        return recyclerListLiveData
    }

    fun makeApiCall() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val retroInstance =
                    RetroInstance.getRetroInstance().create(RetroService::class.java)
                val response = retroInstance.getDataFromApi()
                recyclerListLiveData.postValue(response)
            } catch (e: Exception) {
                recyclerListLiveData.postValue(null)
            }

        }
    }
}
