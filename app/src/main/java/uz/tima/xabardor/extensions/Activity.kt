package uz.tima.xabardor.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.inputmethod.InputMethodManager
import uz.tima.xabardor.rest.models.Tag
import uz.tima.xabardor.rest.models.news.Author
import uz.tima.xabardor.rest.models.news.News
import uz.tima.xabardor.rest.models.rubric.RubricsData
import uz.tima.xabardor.ui.about.AboutUsAcitivity
import uz.tima.xabardor.ui.contact.ContactUsActivity
import uz.tima.xabardor.ui.main.MainActivity
import uz.tima.xabardor.ui.news.NewsActivity
import uz.tima.xabardor.ui.news.author.AuthorActivity
import uz.tima.xabardor.ui.news.favourites.FavouritesActivity
import uz.tima.xabardor.ui.news.list.NewsListActivity
import uz.tima.xabardor.ui.no_connection.NoConnectionActivity
import uz.tima.xabardor.ui.splash.SplashActivity
import uz.tima.xabardor.ui.video.VideoActivity
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun Activity.openMainActivity() {
    startActivityWithoutBack<MainActivity>()
}


fun Activity.openSplashActivity() {
    startActivityWithoutBack<SplashActivity>()
}

fun Activity.openNewsActivity(
        news: News
) {
    startActivity<NewsActivity>(bundle = Bundle().apply {
        putSerializable(NewsActivity.NEWS, news)
    })
}

fun Activity.openNewsListActivity(
        tag: Tag? = null,
        searchText: String? = null
) {
    startActivity<NewsListActivity>(bundle = Bundle().apply {
        putSerializable(NewsListActivity.BUNDLE_TAG, tag)
        putString(NewsListActivity.BUNDLE_SEARCH_TEXT, searchText)
    })
}

fun Activity.openVideoListActivity(
    bundle: Bundle
) {
    startActivity<VideoActivity>(bundle = bundle)
}

fun Activity.openNewsListActivity(
    rubricsData: RubricsData? = null,
    searchText: String? = null
) {
    startActivity<NewsListActivity>(bundle = Bundle().apply {
        putSerializable(NewsListActivity.BUNDLE_TAG, rubricsData)
        putString(NewsListActivity.BUNDLE_SEARCH_TEXT, searchText)
    })
}
fun URL.getFileSize(): Int? {
    return try {
        openConnection().contentLength
    } catch (x: Exception) {
        null
    }
}
fun Activity.openBrowser(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
fun hideKeyBoard(activity:Activity?) {
    val view = activity?.currentFocus ?: View(activity)
    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}
fun View?.blockClickable(
    blockTimeMilles: Long = 500
) {
    this?.isClickable = false
    Handler().postDelayed({ this?.isClickable = true }, blockTimeMilles)
}
fun Activity.openAboutUsActivity() {
    startActivity<AboutUsAcitivity>()
}

fun Activity.openContactUsActivity() {
    startActivity<ContactUsActivity>()
}

fun Activity.openFavouritesActivity() {
    startActivity<FavouritesActivity>()
}

fun Activity.openNoConnectionActivity() {
    startActivityWithoutBack<NoConnectionActivity>()
}

fun Activity.openAuthorActivity(
        author: Author
) {
    startActivity<AuthorActivity>(bundle = Bundle().apply {
        putSerializable(AuthorActivity.BUNDLE_AUTHOR, author)
    })
}


inline fun <reified T : Activity> Activity.startActivity(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

inline fun <reified T : Activity> Activity.startActivityForResult(bundle: Bundle? = null, requestCode: Int) {
    val intent = Intent(this, T::class.java)
    bundle?.let {
        intent.putExtras(it)
    }
    startActivityForResult(intent, requestCode)
}

inline fun <reified T : Activity> Activity.startActivityWithoutBack(bundle: Bundle? = null) {
    val intent = Intent(this, T::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    bundle?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

@SuppressLint("SimpleDateFormat")
fun formatDateStr(strDate: Date): String {
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ")
    formatter.timeZone = TimeZone.getTimeZone("Asia/Tashkent")
    var date = formatter.format(strDate).split("T")
    val day = date.first().split("-").last()
    val month = date.first().split("-")[1]
    val year = date.first().split("-").first()

    val hour = date.last().split(".").first().split(":").first()
    val minute = date.last().split(".").first().split(":")[1]
    val sekund = date.last().split(".").first().split(":").last()


    return "$day.$month.$year ($hour:$minute)"
}