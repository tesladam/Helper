package com.tesladam.navigationdeneme

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.request.JsonArrayRequest
import com.android.volley.request.JsonObjectRequest
import com.android.volley.request.StringRequest
import com.android.volley.toolbox.VolleyTickle
import com.bumptech.glide.Glide
import com.github.underscore.lodash.U
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.tesladam.helper.R
import com.tesladam.helper.Singleton
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

    @JvmStatic
    fun logGoster(itemString: String) {
        val maxLogSize = 1000
        for (i in 0..itemString.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > itemString.length) itemString.length else end
            Log.v("asdasd", itemString.substring(start, end))
        }
    }

    @JvmStatic
    fun sistemDil(vararg diller: String): String {
        for (i in diller.indices)
            if (Locale.getDefault().language == diller[i])
                return diller[i]

        return "en"
    }

    @JvmStatic
    fun doluArray(jsonArray: JSONArray, key: String): JSONArray {
        val newArray = JSONArray()

        for (i in 0 until jsonArray.length()) {
            if (jsonArray.getJSONObject(i).has(key) && jsonArray.getJSONObject(i)[key] != "") {
                newArray.put(jsonArray.getJSONObject(i))
            }
        }

        return newArray
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

class helperAdapterRecycler(
    private val layout: Int,
    private val size: Int,
    private val function: (View, Int) -> Unit
) : RecyclerView.Adapter<helperAdapterRecycler.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(layout, parent, false))
    }

    override fun getItemCount(): Int {
        return size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        function.invoke(holder.itemView, position)
    }

}

object helperBellek {
    @JvmStatic
    fun getBellek(context: Context, bellekKey: String): Any? {
        return context.getSharedPreferences(bellekKey, Context.MODE_PRIVATE).all[bellekKey]
    }

    @JvmStatic
    fun setBellek(context: Context, bellekKey: String, bellekValue: String) {
        val shared = context.getSharedPreferences(bellekKey, Context.MODE_PRIVATE)
        shared.edit().putString(bellekKey, bellekValue).apply()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
class helperBildirim(val context: Context, val title: String, val text: String, val ekran: Class<*>) : ContextWrapper(context) {

    init {
        val channel = NotificationChannel("app", "Bildirim", NotificationManager.IMPORTANCE_DEFAULT)
        channel.enableVibration(true)
        channel.setShowBadge(true)

        val notification = NotificationCompat.Builder(context, "app")
            .setContentTitle(title)
            .setContentText(text)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setAutoCancel(true)
            .addAction(
                NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Git",
                    PendingIntent.getActivity(context, 0, Intent(context, ekran), 0)).build())
            .setContentIntent(PendingIntent.getActivity(context, 0, Intent(context, ekran), 0))
            .build()

        val manager = context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        manager.notify(18, notification)

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

class helperInternetErisimi(val context: Context) : ContextWrapper(context) {
    fun internetErisimi(): Boolean {
        val conMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conMgr.activeNetworkInfo != null && conMgr.activeNetworkInfo.isAvailable && conMgr.activeNetworkInfo.isConnected
    }
}

class helperIzinAl(
    private val context: Context,
    private val izin: String,
    private val function: () -> Unit
) : ContextWrapper(context) {
    init {
        Dexter.withContext(context)
            .withPermission(izin)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    function.invoke()
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    p1?.continuePermissionRequest()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {}
            }).check()
    }
}

class helperJson(private val context: Context) : ContextWrapper(context) {

    val singleton = Singleton(context)
    val tickle = VolleyTickle.newRequestTickle(context)

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

    fun getSirali(url: String, result: (String) -> Unit) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        val stringRequest = StringRequest(Request.Method.GET, url, null, null)
        tickle.add(stringRequest)
        val response = tickle.start()

        if (response.statusCode == 200) {
            val data = VolleyTickle.parseResponse(response)
            result.invoke(data)
        } else
            Log.d("asdasd", "Helper Json getSirali Hata")
    }

    fun getXml(url: String, result: (JSONObject) -> Unit) {
        StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())

        val stringRequest = StringRequest(Request.Method.GET, url, null, null)
        tickle.add(stringRequest)
        val response = tickle.start()

        if (response.statusCode == 200) {
            val data = VolleyTickle.parseResponse(response)
            result.invoke(JSONObject(U.xmlToJson(data)))
        } else
            Log.d("asdasd", "Helper Json getXml Hata")
    }

    fun post( url: String, postKey: HashMap<String, String>,  result: (String) -> Unit ){

        val request = object : StringRequest(Method.POST, url, Response.Listener {
            result.invoke(it)
        }, Response.ErrorListener {
            Log.d("asdasd", "post: $it")
            result.invoke("gönderilemedi")
        }){
            override fun getParams(): MutableMap<String, String> {
                super.getParams()
                Log.d("asdasd", "getParams: $postKey")
                return postKey
            }
        }

        singleton.addToRequestQueue(request)
    }
}

class helperPopUp(private val context: Context, private val title: String = "", private val mesaj: String = "",
                  private val positive: String = "", private val posClick: () -> Unit = {},
                  private val negative: String = "", private val negaClick: () -> Unit = {},
                  private val natural: String = "", private val natuClick: () -> Unit = {}): ContextWrapper(context){
    private val builder = AlertDialog.Builder(context)

    init {
        if (mesaj.isNotEmpty())
            builder.setMessage(mesaj)

        if (title.isNotEmpty())
            builder.setTitle(title)

        if (positive.isNotEmpty())
            builder.setPositiveButton(positive) { _, _ ->  posClick.invoke() }

        if (negative.isNotEmpty())
            builder.setNegativeButton(negative) { _, _ -> negaClick.invoke() }

        if (natural.isNotEmpty())
            builder.setNegativeButton(natural) { _, _ -> natuClick.invoke() }

        builder.create()
    }

    fun show(): AlertDialog { return builder.show()!!}
    fun get(): AlertDialog { return builder.create()!!}
    fun view(view: View): helperPopUp { builder.setView(view); return this }
}

class helperResimYukle(
    val context: Context,
    val url: String,
    val img: ImageView
) : ContextWrapper(context) {
    init {
        Glide.with(context)
            .load(url)
            .into(img)
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
) : CarouselAdapter<adapterSlider.ViewHolder>() {
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



interface dinle {
    fun indiriliyor(yüzde: Double)
    fun bitti(bitti: Boolean)
}

interface bildirim{
    fun bildirim(geldi: Boolean, data: String)
}