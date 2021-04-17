package uz.xabardor.extensions

import android.content.Context
import android.net.ConnectivityManager

fun Context.isNotOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.getActiveNetworkInfo()
    return !(netInfo != null && netInfo.isConnectedOrConnecting())
}

fun Context.isOnline(): Boolean = !isNotOnline()