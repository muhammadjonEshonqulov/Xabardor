package uz.tima.xabardor.ui.about

import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.TextView
import android.widget.Toast
import moxy.presenter.InjectPresenter
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.Krill
import uz.tima.xabardor.extensions.language.Uzbek
import uz.tima.xabardor.rest.models.AboutResponse
import uz.tima.xabardor.ui.about.AboutUsPresenter
import uz.tima.xabardor.ui.about.AboutUsView
import uz.tima.xabardor.ui.base.BaseActivity

class AboutUsAcitivity : BaseActivity(), AboutUsView {


    @InjectPresenter
    lateinit var presenter: AboutUsPresenter

    override val layoutId: Int
        get() = R.layout.activity_about

    lateinit var text_view: TextView

    override fun setupToolbar() {
        toolBarTitleTextView?.setText(R.string.about_us)
    }

    override fun onCreatedView() {
        text_view = findViewById(R.id.text_view)
        presenter.getAbout()
    }

    override fun onClick(v: View?) {

    }

    override fun onErrorNewsList(throwable: Throwable) {
        Toast.makeText(this, "" + throwable.message, Toast.LENGTH_SHORT).show()
    }

    override fun onSuccessNewsList(elem: List<AboutResponse>) {
        if (languageManager.currentLanguage.id == Uzbek().id) {
            elem.get(0).content?.let {
                text_view.text = ""+Html.fromHtml(it)
            }
        } else if (languageManager.currentLanguage.id == Krill().id) {
            elem.get(0).content_cyrl?.let {
                text_view.text = ""+Html.fromHtml(it)
            }
        }
        text_view.movementMethod = LinkMovementMethod.getInstance();
    }
}