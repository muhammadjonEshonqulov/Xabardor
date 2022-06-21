package uz.tima.xabardor.ui.splash

import android.os.Handler
import android.os.Looper
import android.view.View
import moxy.presenter.InjectPresenter
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.openMainActivity
import uz.tima.xabardor.ui.base.BaseActivity

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
        Handler(Looper.getMainLooper()).postDelayed({
            openMainActivity()
        }, 500)
    }

    override fun onSuccessTags() {
        Handler(Looper.getMainLooper()).postDelayed({
            openMainActivity()
        }, 500)
    }

}