package uz.tima.xabardor.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import uz.tima.xabardor.BuildConfig

@SuppressLint("MissingPermission")
fun Context.isNotOnline(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.activeNetworkInfo
    return !(netInfo != null && netInfo.isConnectedOrConnecting())
}

fun Context.isOnline(): Boolean = !isNotOnline()

fun lg(text: String) {
    if (BuildConfig.isDebug) {
        Log.d("XABARDOR", text)
    }
}