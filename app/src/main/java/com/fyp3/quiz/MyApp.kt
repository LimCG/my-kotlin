package com.fyp3.quiz

import android.app.Application
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class MyApp : Application() {

    companion object {

        private val TAG = MyApp::class.java.simpleName

        @get:Synchronized var instance : MyApp? = null
        private set

        private var requestQueue : RequestQueue? = null

    }

    override fun onCreate() {
        super.onCreate()

        instance = this

        MyPref.init(this)

        if (requestQueue == null) {

            requestQueue =  Volley.newRequestQueue(this)

        }

    }

    fun <T> addToRequestQueue(request: Request<T>, tag: String) {
        request.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        requestQueue?.add(request)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        request.tag = TAG
        requestQueue?.add(request)
    }

    fun cancelPendingRequests(tag: Any) {
        if (requestQueue != null) {
            requestQueue!!.cancelAll(tag)
        }
    }

}