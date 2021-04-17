package uz.xabardor.ui.no_connection

import android.view.View
import android.widget.Button
import uz.xabardor.R
import uz.xabardor.extensions.isOnline
import uz.xabardor.extensions.openSplashActivity
import uz.xabardor.ui.base.BaseActivity

class NoConnectionActivity : BaseActivity() {
    override val layoutId: Int
        get() = R.layout.activity_no_connection

    lateinit var retryButton: Button

    override fun setupToolbar() {
        toolBarBackImageView?.visibility = View.INVISIBLE
        toolBarLogoImageView?.visibility = View.VISIBLE
    }

    override fun onCreatedView() {
        retryButton = findViewById(R.id.button_retry)


        retryButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            retryButton -> {
                if (isOnline()) {
                    openSplashActivity()
                }
            }
        }
    }
}