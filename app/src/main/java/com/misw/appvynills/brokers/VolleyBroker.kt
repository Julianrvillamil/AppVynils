package com.misw.appvynills.brokers

import android.content.Context
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class VolleyBroker constructor(context: Context) {

    val instance: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    companion object{
        const val BASE_URL= "http://10.0.2.2:3000/"
        fun getRequest(
            path:String
            , responseListener: Response.Listener<String>
            , errorListener: Response.ErrorListener): StringRequest {
                            return StringRequest(Request.Method.GET, BASE_URL+path, responseListener,errorListener)
            }
        fun postRequest(
            path: String,
            body: JSONObject,
            responseListener: Response.Listener<JSONObject>,
            errorListener: Response.ErrorListener ):JsonObjectRequest{
                return  JsonObjectRequest(Request.Method.POST, BASE_URL+path, body, responseListener, errorListener)
            }

    }
}