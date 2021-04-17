package uz.xabardor.extensions

import android.content.res.Resources

fun Int.dpToPx(): Int {
    return (this * Resources.getSystem().getDisplayMetrics().density).toInt()
}