package uz.xabardor.extensions

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.about.AboutUsAcitivity
import uz.xabardor.ui.contact.ContactUsAcitivity
import uz.xabardor.ui.main.MainActivity
import uz.xabardor.ui.news.NewsActivity
import uz.xabardor.ui.news.author.AuthorActivity
import uz.xabardor.ui.news.favourites.FavouritesActivity
import uz.xabardor.ui.news.list.NewsListActivity
import uz.xabardor.ui.no_connection.NoConnectionActivity
import uz.xabardor.ui.splash.SplashActivity

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

fun Activity.openBrowser(url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setData(Uri.parse(url))
        startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}


fun Activity.openAboutUsActivity() {
    startActivity<AboutUsAcitivity>()
}

fun Activity.openContactUsActivity() {
    startActivity<ContactUsAcitivity>()
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