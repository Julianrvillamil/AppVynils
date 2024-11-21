package com.misw.appvynills.brokers

import android.content.Context
import android.util.Log
import com.android.volley.RequestQueue
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.misw.appvynills.utils.Constants
import org.json.JSONObject


class VolleyBroker constructor(context: Context) {

    val instance: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    companion object{
        fun getRequest(
            path:String
            , responseListener: Response.Listener<String>
            , errorListener: Response.ErrorListener): StringRequest {
                            return StringRequest(
                                Request.Method.GET,
                                Constants.BASE_URL+path,
                                responseListener,
                                errorListener)
            }
        fun postRequest(
            path: String,
            body: JSONObject,
            responseListener: Response.Listener<JSONObject>,
            errorListener: Response.ErrorListener
        ):JsonObjectRequest{
            Log.i("VolleyBroker", "Preparando solicitud POST para: ${Constants.BASE_URL + path}")
            Log.i("VolleyBroker", "Cuerpo de la solicitud POST: $body")
                return  object: JsonObjectRequest(
                    Request.Method.POST,
                    Constants.BASE_URL+path,
                    body,
                    responseListener,
                    errorListener) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = mutableMapOf<String, String>()
                        headers["Content-Type"] = "application/json"
                        Log.i("VolleyBroker", "Encabezados de la solicitud POST: $headers")
                        return headers
                    }
                }
            }


    }
}