package com.example.todoapp.data.network

import android.util.Log
import com.example.todoapp.data.model.TodoItem
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface RequestCallback {
    fun onSuccess(response: String)
    fun onFailure(error: String)
}

//@Singleton
class ApiService @Inject constructor(
    private var client: OkHttpClient
    ) {

    // @GET request
    fun getRequest(url: String){
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        throw IOException("Запрос к серверу не был успешен:" +
                                " ${response.code} ${response.message}")
                    }
//                    callback.onSuccess(response.body!!.string())
                    // пример получения всех заголовков ответа
//                    for ((name, value) in response.headers) {
//                        Log.i("mApiService","$name: $value")
//                    }
                    // вывод тела ответа
                    val responseString = response.body!!.string()
                    val receivedTodoItem = Gson().fromJson(responseString, TodoItem::class.java)
                    Log.i("mApiService", responseString)
                    Log.i("mApiService_2", receivedTodoItem.id.toString())
                    Log.i("mApiService_2", receivedTodoItem.textCase)
                    Log.i("mApiService_2", receivedTodoItem.deadlineData)
                }
            }
        })
    }

    // @POST request
   fun postRequest(url: String, jsonRequest: String, callback: RequestCallback){
       val body = jsonRequest.toRequestBody()
       val request = Request.Builder().url(url).post(body).build()

       client.newCall(request).enqueue(object : Callback {

           override fun onResponse(call: Call, response: Response) {
               response.use {
                   if (!response.isSuccessful) {
                       throw Exception("Запрос к серверу не был успешен:" +
                               " ${response.code} ${response.message}")
                   }
                   callback.onSuccess(response.body!!.string())
               }
           }

           override fun onFailure(call: Call, e: IOException) {
               callback.onFailure(e.toString());
           }
       })
   }
}