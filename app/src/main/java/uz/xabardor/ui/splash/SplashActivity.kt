package uz.xabardor.ui.splash

import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.extensions.openMainActivity
import uz.xabardor.ui.base.BaseActivity

class SplashActivity : BaseActivity(), SplashView {

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    override val layoutId: Int
        get() = R.layout.activity_splash

    override fun setupToolbar() {

    }

    override fun onCreatedView() {

    }

    override fun onClick(p0: View?) {

    }

    override fun onLoadingTags() {

    }

    override fun onErrorTags(throwable: Throwable) {

    }

    override fun onSuccessTags() {
        Handler(Looper.getMainLooper()).postDelayed({
            openMainActivity()
        }, 500)
    }

}