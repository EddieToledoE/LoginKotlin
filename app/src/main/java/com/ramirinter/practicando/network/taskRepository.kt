package com.ramirinter.practicando.network


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class TaskRepository {

    private val api = RetrofitInstance.api

    fun getTasks(onResult: (List<Task>?, String?) -> Unit) {
        api.getTasks().enqueue(object : Callback<List<Task>> {
            override fun onResponse(call: Call<List<Task>>, response: Response<List<Task>>) {
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    onResult(null, "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Task>>, t: Throwable) {
                onResult(null, "Error de red: ${t.message}")
            }
        })
    }

    fun deleteTask(taskId: String, onResult: (Boolean) -> Unit) {
        api.deleteTask(taskId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                onResult(response.isSuccessful)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                onResult(false)
            }
        })
    }





    fun updateTask(task: Task, onResult: (Task?, String?) -> Unit) {
        val updateTask = UpdateTask(task.title, task.description, task.status)
        val call = api.updateTask(task.id, updateTask)

        call.enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                Log.d("API Request", "Request: ${call.request().toString()}")

                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    Log.e("API Response", "Error ${response.code()}: ${response.message()}")
                    Log.e("API Response", "Response Body: ${response.errorBody()?.string()}")
                    onResult(null, "Error ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                Log.e("API Request", "Failure: ${t.message}")
                onResult(null, t.message)
            }
        })
    }

    fun getTaskById(taskId: String, onResult: (Task?, String?) -> Unit) {
        api.getTaskById(taskId).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                Log.d("API Request", "Request: ${call.request().toString()}")
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    Log.e("API Response", "Error ${response.code()}: ${response.message()}")
                    Log.e("API Response", "Response Body: ${response.errorBody()?.string()}")
                    onResult(null, "Error ${response.code()}: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                onResult(null, "Error de red: ${t.message}")
            }
        })
    }


    fun createTask(task: CreateTaskRequest, onResult: (Task?, String?) -> Unit) {
        val createTaskRequest = CreateTaskRequest(task.title, task.description)
        api.createTask(createTaskRequest).enqueue(object : Callback<Task> {
            override fun onResponse(call: Call<Task>, response: Response<Task>) {
                Log.d("API Request", "Request: ${call.request().toString()}")
                if (response.isSuccessful) {
                    onResult(response.body(), null)
                } else {
                    Log.e("API Response", "Error ${response.code()}: ${response.message()}")
                    Log.e("API Response", "Response Body: ${response.errorBody()?.string()}")
                    onResult(null, "Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Task>, t: Throwable) {
                onResult(null, "Error de red: ${t.message}")
            }
        })
    }

}
