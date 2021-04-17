package uz.xabardor.app

import android.app.Application
import android.content.Context
import uz.xabardor.database.BaseDatabase

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        context = this.applicationContext

        BaseDatabase.context = applicationContext

    }


    companion object{
        private lateinit var context: Context
    }

}