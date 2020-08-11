package com.tesladam.helper

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tesladam.navigationdeneme.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val TAG = "asdasd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helperJson(this).get("https://tesladam.herokuapp.com/res/$packageName", helper.obje){
            Log.d(TAG, "onCreate: $it")
        }
        helperJson(this).get("https://tesladam.herokuapp.com/res/$packageName", helper.obje){
            Log.d(TAG, "onCreate: $it")
        }

    }
}