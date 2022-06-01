package uz.xabardor.ui.base

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import moxy.MvpAppCompatActivity
import uz.xabardor.R
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.LanguageManager
import uz.xabardor.extensions.openNoConnectionActivity
import uz.xabardor.rest.services.BaseService
import uz.xabardor.rest.services.NoConnectionListener
import java.util.*

abstract class BaseActivity : MvpAppCompatActivity(), View.OnClickListener, NoConnectionListener {

    var toolBar: Toolbar? = null
    var toolBarLogoImageView: ImageView? = null
    var toolBarMenuImageView: ImageView? = null
    var toolBarBackImageView: ImageView? = null
    var toolBarSearchImageView: ImageView? = null
    var toolBarFavouriteImageView: ImageView? = null
    var toolBarShareImageView: ImageView? = null
    lateinit var languageManager: LanguageManager

    var toolBarSearchEditText: EditText? = null

    var toolBarTitleTextView: TextView? = null

    abstract val layoutId: Int get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        toolBar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolBar)
        languageManager = LanguageManager(this)

//        notifyLanguageChanged()

        toolBarMenuImageView = findViewById(R.id.image_view_toolbar_menu)
        toolBarLogoImageView = findViewById(R.id.image_view_toolbar_logo)
        toolBarBackImageView = findViewById(R.id.image_view_toolbar_back)
        toolBarSearchImageView = findViewById(R.id.image_view_toolbar_search)
        toolBarFavouriteImageView = findViewById(R.id.image_view_toolbar_favourite)
        toolBarShareImageView = findViewById(R.id.image_view_toolbar_share)

        toolBarTitleTextView = findViewById(R.id.text_view_toolbar_title)
        toolBarSearchEditText = findViewById(R.id.edit_text_toolbar_search)

        toolBarBackImageView?.setOnClickListener {
            finish()
        }

        toolBarSearchImageView?.setOnClickListener(this)
        toolBarFavouriteImageView?.setOnClickListener(this)
        toolBarShareImageView?.setOnClickListener(this)
//        toolBarTitleTextView?.setOnClickListener(this)


        setupToolbar()
        onCreatedView()
    }

    override fun onResume() {
        super.onResume()

        BaseService.noConnectionListener = this
    }

    override fun onNoConnection() {
        Handler(Looper.getMainLooper()).post {
            openNoConnectionActivity()
        }
    }



    abstract fun setupToolbar()

    abstract fun onCreatedView()
    protected fun notifyLanguageChanged() = onCreateLanguage(languageManager.currentLanguage)


    open fun onCreateLanguage(language: Language) {

        val configuration = Configuration()
        configuration.setLocale(
            Locale(languageManager.currentLanguage.userName)
        )
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

}