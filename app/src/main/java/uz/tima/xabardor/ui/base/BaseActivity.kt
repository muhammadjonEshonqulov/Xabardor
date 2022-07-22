package uz.tima.xabardor.ui.base

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.android.synthetic.main.activity_news.*
import moxy.MvpAppCompatActivity
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.Language
import uz.tima.xabardor.extensions.language.LanguageManager
import uz.tima.xabardor.extensions.lg
import uz.tima.xabardor.extensions.openNoConnectionActivity
import uz.tima.xabardor.rest.services.BaseService
import uz.tima.xabardor.rest.services.NoConnectionListener
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

    private var appUpdateManager: AppUpdateManager? = null
    private val APP_UPDATE = 100

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

        if (appUpdateManager == null) {
            appUpdateManager = AppUpdateManagerFactory.create(this)
        }

        appUpdateManager?.appUpdateInfo?.addOnSuccessListener {
            lg("it.updateAvailability() ->"+it.updateAvailability())
            lg("UpdateAvailability.UPDATE_AVAILABLE ->"+ UpdateAvailability.UPDATE_AVAILABLE)
            lg("it.isUpdateTypeAllowed(FLEXIBLE)->"+it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
            lg("it.isUpdateTypeAllowed(IMMEDIATE)->"+it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                if (it.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){
                    appUpdateManager?.startUpdateFlowForResult(it, AppUpdateType.FLEXIBLE, this, APP_UPDATE)

                } else if (it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                    appUpdateManager?.startUpdateFlowForResult(it, AppUpdateType.IMMEDIATE, this, APP_UPDATE)

                }
            }
        }
        appUpdateManager?.registerListener(installStateUpdatedListener)
    }

    private val installStateUpdatedListener = InstallStateUpdatedListener {
        if (it.installStatus() == InstallStatus.DOWNLOADED) {
            showCompletedUpdate()
        }
    }

    private fun showCompletedUpdate() {
        val snackbar = Snackbar.make(recycler_view_description, getString(R.string.read_to_install), Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction(getString(R.string.install)) {
            appUpdateManager?.completeUpdate()
        }
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == APP_UPDATE && resultCode != RESULT_OK) {
            Toast.makeText(this, getString(R.string.cancelled_intall), Toast.LENGTH_SHORT).show()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStop() {
        if (appUpdateManager != null) {
            appUpdateManager?.unregisterListener(installStateUpdatedListener)
        }
        super.onStop()

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