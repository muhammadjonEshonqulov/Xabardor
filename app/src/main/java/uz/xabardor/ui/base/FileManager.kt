package uz.xabardor.ui.base

import android.content.Context
import android.util.Base64
import java.io.File


class FileManager(val context: Context) {

    var processUpdate: OnProcessUpdate? = null

    var cancel: Boolean = false

    val APP_FILE_DIRECTORY_PATH: String =
        "${context.filesDir}/Xabardor/"

    fun hasOfflineFile(url: String, dir: String, isEncrypt: Boolean): Boolean {
        return try {
            File("$APP_FILE_DIRECTORY_PATH$dir/", convertUrlToStoragePath(url, isEncrypt)).exists()
        } catch (ex: Exception) {
            false
        }
    }

    fun convertUrlToStoragePath(url: String, isEncrypt: Boolean): String {
        var filename: String = url.split("/").last()
        val tpye= filename.split(".").last()
        if (isEncrypt) {
            filename = Base64.encodeToString(filename.toByteArray(), Base64.NO_WRAP)
        }
        return "$filename.$tpye"
    }

    interface OnProcessUpdate {
        fun onUpdate(process: Int)
    }

}