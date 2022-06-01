package uz.xabardor.app

import android.app.Application
import android.content.Context
import com.downloader.PRDownloader
import com.downloader.PRDownloaderConfig
import uz.xabardor.database.BaseDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext

        BaseDatabase.context = applicationContext

        PRDownloader.initialize(
            this,
            PRDownloaderConfig.newBuilder().setDatabaseEnabled(true).setReadTimeout(3000)
                .setConnectTimeout(3000).build()
        )

    }


    companion object{
         lateinit var context: Context
    }

}