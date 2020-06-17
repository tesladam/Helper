package com.tesladam.helper

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.github.vejei.carouselview.CarouselAdapter
import io.github.vejei.carouselview.CarouselView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object helper : Application() {
    @JvmStatic
    val vertical = 1

    @JvmStatic
    val horizontal = 0

    @JvmStatic
    fun manager(yon: Int): LinearLayoutManager {
        return LinearLayoutManager(this, yon, false)
    }

    @JvmStatic
    val array = 1

    @JvmStatic
    val obje = 0

    @JvmStatic
    @Throws(JSONException::class)
    fun appendArray(vararg arrs: JSONArray): JSONArray {
        val result = JSONArray()
        for (arr in arrs)
            for (i in 0 until arr.length())
                result.put(arr.get(i))
        return result
    }

    @JvmStatic
    @Throws(JSONException::class)
    fun appendObje(vararg arrs: JSONObject): JSONArray {
        val result = JSONArray()
        for (arr in arrs)
            result.put(arr)

        return result
    }

    @JvmStatic
    fun dateToString(stringDate: String): String {
        val inFormat = SimpleDateFormat("yyyy-MM-dd", Locale("tr"))
        val date = inFormat.parse(stringDate)
        val outFormat = DateFormat.getDateInstance(DateFormat.LONG)
        return outFormat.format(date)
    }
}

class helperizinAl(
    private val context: Context,
    private val izin: String,
    private val function: () -> Unit
) :
    ContextWrapper(context) {
    init {
        Dexter.withContext(context)
            .withPermission(izin)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    function.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

                }
            }).check()
    }
}

object helperFragment {

    fun degistir(fragment: Fragment, gidilecekEkran: Int) {
        findNavController(fragment).navigate(gidilecekEkran)
    }

    fun degistir(
        fragment: Fragment,
        gidilecekEkran: Int,
        key: String,
        value: Any
    ) {
        findNavController(fragment).navigate(
            gidilecekEkran,
            bundleOf(key to value)
        )
    }

    fun degistir(
        fragment: Fragment,
        gidilecekEkran: Int,
        silinecekEkran: Int
    ) {
        findNavController(fragment).navigate(
            gidilecekEkran,
            null,
            NavOptions.Builder().setPopUpTo(silinecekEkran, true).build()
        )
    }

    fun degistir(
        fragment: Fragment,
        gidilecekEkran: Int,
        key: String,
        value: Any,
        silinecekEkran: Int
    ) {
        findNavController(fragment).navigate(
            gidilecekEkran,
            bundleOf(key to value),
            NavOptions.Builder().setPopUpTo(silinecekEkran, true).build()
        )
    }

    fun geri(fragment: Fragment) {
        fragment.activity?.onBackPressed()
    }
}

class helperActivity(private val context: Context) : ContextWrapper(context) {
    fun degistir(ekran: Class<*>) {
        startActivity(Intent(this, ekran))
    }

    fun degistir(ekran: Class<*>, key: String, value: String) {
        startActivity(Intent(this, ekran).putExtra(key, value))
    }

    fun degistir(ekran: Class<*>, key: String, value: Int) {
        startActivity(Intent(this, ekran).putExtra(key, value))
    }

    fun degistirSil(ekran: Class<*>) {
        startActivity(
            Intent(this, ekran).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }

    fun degistirSil(ekran: Class<*>, key: String, value: String) {
        startActivity(
            Intent(this, ekran).putExtra(key, value).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        )
    }

    fun degistirSil(ekran: Class<*>, key: String, value: Int) {
        startActivity(
            Intent(this, ekran).putExtra(key, value).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
    }
}

class helperJson(private val context: Context) : ContextWrapper(context) {

    val singleton = Singleton(context)

    fun get(url: String, tur: Int, result: (Any) -> Unit) {
        if (tur == helper.array) {
            val arrayRequest = JsonArrayRequest(url, Response.Listener {
                result.invoke(it)
            }, Response.ErrorListener { })
            singleton.addToRequestQueue(arrayRequest)
        } else {
            val objeRequest = JsonObjectRequest(url, null, Response.Listener {
                result.invoke(it)
            }, Response.ErrorListener { })
            singleton.addToRequestQueue(objeRequest)
        }
    }

}

class helperSlider(private val carouselView: CarouselView) {
    fun slider(): CarouselView {
        carouselView.mode = CarouselView.Mode.SNAP
        carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.NORMAL
        return carouselView
    }

    fun carousel1(): CarouselView {
        carouselView.itemMargin = 10 * 3
        carouselView.mode = CarouselView.Mode.PREVIEW
        carouselView.previewOffset = 30 * 3
        carouselView.previewSide = CarouselView.PreviewSide.SIDE_BY_SIDE
        carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.NORMAL
        return carouselView
    }

    fun carousel2(): CarouselView {
        carouselView.itemMargin = 10 * 3
        carouselView.mode = CarouselView.Mode.PREVIEW
        carouselView.previewOffset = 30 * 3
        carouselView.previewScaleFactor = 0.2.toFloat()
        carouselView.previewSide = CarouselView.PreviewSide.SIDE_BY_SIDE
        carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.SCALE
        return carouselView
    }
}

class adapterSlider(
    private val layout: Int,
    private val size: Int,
    private val function: (View, Int) -> Unit
) :
    CarouselAdapter<adapterSlider.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreatePageViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getPageCount(): Int {
        return size
    }

    override fun onBindPageViewHolder(holder: ViewHolder, position: Int) {
        function.invoke(holder.itemView, position)
    }

}