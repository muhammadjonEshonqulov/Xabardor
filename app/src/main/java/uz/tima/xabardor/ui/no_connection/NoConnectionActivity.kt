package uz.tima.xabardor.ui.no_connection

import android.view.View
import android.widget.Button
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.isOnline
import uz.tima.xabardor.extensions.openSplashActivity
import uz.tima.xabardor.ui.base.BaseActivity

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