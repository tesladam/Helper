package com.tesladam.navigationdeneme

import android.app.*
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
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
import cn.pedant.SweetAlert.SweetAlertDialog
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
import com.tesladam.helper.BuildConfig
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

private var helper_guvenlik: helperGuvenlik? = null

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
        if (helper_guvenlik != null){
            var bosMu = true

            helper_guvenlik?.kontrol(object : Guvenlik{
                override fun Kontrol(sonuc: Boolean) {
                    bosMu = sonuc
                }
            })

            if (bosMu)
                return size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        function.invoke(holder.itemView, position)
    }

}

object helperBellek {
    @JvmStatic
    fun getBellek(context: Context, bellekKey: String): Any? {
        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        var bosMu = true

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                bosMu = sonuc
            }
        })

        if (bosMu){
            return context.getSharedPreferences(bellekKey, Context.MODE_PRIVATE).all[bellekKey]
        }
        return null
    }

    @JvmStatic
    fun setBellek(context: Context, bellekKey: String, bellekValue: String) {
        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        var bosMu = true

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                bosMu = sonuc
            }
        })

        if (bosMu){
            val shared = context.getSharedPreferences(bellekKey, Context.MODE_PRIVATE)
            shared.edit().putString(bellekKey, bellekValue).apply()
        }
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

class helperGuvenlik(private val context: Context) : ContextWrapper(context){

    companion object{
        private var mCtx: Context? = null
        private var res = false
    }

    private var ctx1: Context? = null

    val ctx2: Context?
        get() {
            if (ctx1 == null)
                ctx1 = mCtx
            return ctx1
        }

    init {
        mCtx = context

/*        ctx = context
        val json = helperJson(applicationContext)
        json.singleton.requestQueue?.cache?.clear()

        if (helperInternetErisimi(applicationContext).internetErisimi()){
            json.getSirali("https://tesladam.herokuapp.com/res/$packageName"){
                val sonuc = JSONObject(it)

                //Uygulama Güvenliği
                guvenlik = sonuc.getInt("status") == 1

                //Version Kontrollü
                if (guvenlik!!){
                    if (sonuc.getInt("version_status") != -1 && sonuc.getInt("version_status") == 1 &&
                        sonuc.getString("version") != "-1" && sonuc.getString("version") != context.packageManager.getPackageInfo(packageName, 0).versionName){

                        val diller = sonuc.getString("version_lang").split(",")
                        var dil = ""

                        if (diller.size == 1)
                            dil = diller[0]

                        val title = if(dil.equals("tr")) "Uyarı" else if (dil.equals("en")) "Warning" else "Предупреждение"

                        val message = if(dil.equals("tr")) "Uygulamanın yeni versiyonu mevcut. Lütfen uygulamayı güncelleyin."
                        else if (dil.equals("en")) "New version of the application is available. Please update the application."
                        else "Доступна новая версия приложения. Пожалуйста, обновите приложение."

                        val tamam = if(dil.equals("tr")) "Tamam" else if (dil.equals("en")) "Okay" else "Ладно"

                        helperPopUp(this, title, message, tamam, {
                            //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.veryansintv.app")))
                        }).show()
                        guvenlik = false
                    }
                }

            }
        }
*/
    }

    fun kontrol(guvenlik: Guvenlik){

        val json = helperJson(ctx2!!)
        json.singleton.requestQueue?.cache?.clear()

        if (!res && helperInternetErisimi(ctx2!!).internetErisimi()){
            json.getSirali("https://tesladam.herokuapp.com/res/$packageName"){
                val sonuc = JSONObject(it)

                //Uygulama Güvenliği
                res = sonuc.getInt("status") == 1
                Log.d("asdasd", "Uygulama Güvenliği: $res")

                //Version Kontrollü
                if (res){
                    if (sonuc.getInt("version_status") != -1 && sonuc.getInt("version_status") == 1 &&
                        sonuc.getString("version") != "-1" && sonuc.getString("version") != ctx2!!.packageManager.getPackageInfo(packageName, 0).versionName){

                        Log.d("asdasd", "Version Kontrollü: true")
                        val diller = sonuc.getString("version_lang").split(",")
                        var dil = ""

                        if (diller.size == 1)
                            dil = diller[0]

                        val title = if(dil.equals("tr")) "Uyarı" else if (dil.equals("en")) "Warning" else "Предупреждение"

                        val message = if(dil.equals("tr")) "Uygulamanın yeni versiyonu mevcut. Lütfen uygulamayı güncelleyin."
                        else if (dil.equals("en")) "New version of the application is available. Please update the application."
                        else "Доступна новая версия приложения. Пожалуйста, обновите приложение."

                        val tamam = if(dil.equals("tr")) "Tamam" else if (dil.equals("en")) "Okay" else "Ладно"

                        helperPopUp(this, title, message, tamam, {
                            //startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.veryansintv.app")))
                        }).show()
                    }
                }
            }
        }

        guvenlik.Kontrol(res)
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
        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                if (sonuc){
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
        })
    }
}

class helperJson(private val context: Context) : ContextWrapper(context) {

    val singleton = Singleton(context)
    val tickle = VolleyTickle.newRequestTickle(context)

    fun get(url: String, tur: Int, result: (Any) -> Unit) {
        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                if (sonuc){
                    if (tur == helper.array) {
                        val arrayRequest = JsonArrayRequest(url, {
                            result.invoke(it)
                        }, { })
                        singleton.addToRequestQueue(arrayRequest)
                    }
                    else {
                        val objeRequest = JsonObjectRequest(url, null, {
                            result.invoke(it)
                        }, { })
                        singleton.addToRequestQueue(objeRequest)
                    }
                }
            }
        })
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

        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                if (sonuc){
                    val stringRequest = StringRequest(Request.Method.GET, url, null, null)
                    tickle.add(stringRequest)
                    val response = tickle.start()

                    if (response.statusCode == 200) {
                        val data = VolleyTickle.parseResponse(response)
                        result.invoke(JSONObject(U.xmlToJson(data)))
                    } else
                        Log.d("asdasd", "Helper Json getXml Hata")
                }
            }
        })
    }

    fun post( url: String, postKey: HashMap<String, String>,  result: (String) -> Unit ){

        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                if (sonuc){
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
        })
    }
}

class helperPopUp(private val context: Context, private val title: String = "", private val mesaj: String = "",
                  private val positive: String = "", private val posClick: () -> Unit = {},
                  private val negative: String = "", private val negaClick: () -> Unit = {},
                  private val natural: String = "", private val natuClick: () -> Unit = {}): ContextWrapper(context){
    private val builder = AlertDialog.Builder(context)
    private var bosMu = true

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
            builder.setNeutralButton(natural) { _, _ -> natuClick.invoke() }

        builder.create()

        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                bosMu = sonuc
            }
        })
    }

    fun show(): AlertDialog? {
        if (bosMu)
            return builder.show()!!
        return null
    }
    fun get(): AlertDialog? {
        if (bosMu)
            return builder.create()!!
        return null
    }
    fun view(view: View): helperPopUp? {
        if (bosMu){
            builder.setView(view)
            return this
        }
        return null
    }
}

class helperPopUp2(private val context: Context, private val title: String = "", private val mesaj: String = "",
                   private val positive: String = "", private val posClick: () -> Unit = {},
                   private val negative: String = "", private val negaClick: () -> Unit = {},
                   private val natural: String = "", private val natuClick: () -> Unit = {}): ContextWrapper(context){

    init {
        val alert = SweetAlertDialog(context)
        alert.setCancelable(false)

        if (title.isNotEmpty())
            alert.titleText = title
        if (mesaj.isNotEmpty())
            alert.contentText = mesaj
        if (positive.isNotEmpty()){
            alert.confirmText = positive
            alert.setConfirmClickListener { posClick }
        }
        if (negative.isNotEmpty()){
            alert.cancelText = negative
            alert.setCancelClickListener { negaClick }
        }
        if (natural.isNotEmpty()){
            alert.setNeutralText(natural)
            alert.setNeutralClickListener { natuClick }
        }

        alert.show()
    }
}

class helperResimYukle(
    val context: Context,
    val url: String,
    val img: ImageView
) : ContextWrapper(context) {
    init {
        if (helper_guvenlik == null)
            helper_guvenlik = helperGuvenlik(context)

        helper_guvenlik?.kontrol(object : Guvenlik{
            override fun Kontrol(sonuc: Boolean) {
                if (sonuc){
                    Glide.with(context)
                        .load(url)
                        .into(img)
                }
            }
        })
    }
}

class helperSlider(private val carouselView: CarouselView) {

    private var bosMu = true

    init {
        if (helper_guvenlik == null)
            bosMu = false
    }

    fun slider(): CarouselView? {
        if (bosMu){
            carouselView.mode = CarouselView.Mode.SNAP
            carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.NORMAL
            return carouselView
        }
        return carouselView
    }

    fun carousel1(): CarouselView? {
        if (bosMu){
            carouselView.itemMargin = 10 * 3
            carouselView.mode = CarouselView.Mode.PREVIEW
            carouselView.previewOffset = 30 * 3
            carouselView.previewSide = CarouselView.PreviewSide.SIDE_BY_SIDE
            carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.NORMAL
            return carouselView
        }
        return carouselView
    }

    fun carousel2(): CarouselView? {
        if (bosMu){
            carouselView.itemMargin = 10 * 3
            carouselView.mode = CarouselView.Mode.PREVIEW
            carouselView.previewOffset = 30 * 3
            carouselView.previewScaleFactor = 0.2.toFloat()
            carouselView.previewSide = CarouselView.PreviewSide.SIDE_BY_SIDE
            carouselView.sideBySideStyle = CarouselView.PreviewSideBySideStyle.SCALE
            return carouselView
        }
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

interface Guvenlik{
    fun Kontrol(sonuc: Boolean)
}