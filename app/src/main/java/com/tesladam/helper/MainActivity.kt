package com.tesladam.helper

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tesladam.helper.Indicator.indicator.CircleIndicator
import com.tesladam.navigationdeneme.*
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    val TAG = "asdasd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        helperJson(this).get("https://www.veryansintv.com/api/get/posts/is_slider/1/slider_order/ASC/0/20", helper.array){
            val array = JSONArray(it.toString())

            val slider = helperSlider(carousel).slider()
            val adapter = adapterSlider(R.layout.item_slider, array.length()){ view: View, i: Int ->
                helperResimYukle(
                    this,
                    "https://www.veryansintv.com/assets/media/slider/${array.getJSONObject(i)?.getString("slider_image_url")}",
                    view.findViewById(R.id.item_slider))
            }
            slider?.adapter = adapter

            indicator.setWithViewPager2(slider?.viewPager2, false)
            indicator.itemCount = array.length()
            indicator.setAnimationMode(CircleIndicator.AnimationMode.SLIDE)
        }

    }
}