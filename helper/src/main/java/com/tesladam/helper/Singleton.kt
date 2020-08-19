package com.tesladam.helper

import android.content.Context
import com.tesladam.helper.Volley.Request
import com.tesladam.helper.Volley.RequestQueue
import com.tesladam.helper.Volley.Volley

class Singleton(context: Context) {

    private var mRequestQueue: RequestQueue? = null

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx?.applicationContext)
            }
            return mRequestQueue
        }

    init {
        mCtx = context
        mRequestQueue = requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue?.add(req)
    }

    companion object {
        private var mInstance: Singleton? = null
        private var mCtx: Context? = null

        @Synchronized
        fun getInstance(context: Context?): Singleton? {
            if (mInstance == null) {
                mInstance = Singleton(context!!)
            }
            return mInstance
        }
    }
}