package uz.xabardor.ui.contact

import android.content.Intent
import android.net.Uri
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.extensions.openBrowser
import uz.xabardor.rest.services.BaseService
import uz.xabardor.ui.base.BaseActivity


class ContactUsAcitivity : BaseActivity(), ContactUsView {


    @InjectPresenter
    lateinit var presenter: ContactUsPresenter

    override val layoutId: Int
        get() = R.layout.activity_about

    lateinit var webView: WebView

    override fun setupToolbar() {
        toolBarTitleTextView?.setText(R.string.contact_us)
    }

    override fun onCreatedView() {
        webView = findViewById(R.id.web_view)
        webView.loadUrl(BaseService.BASE_API_URL + "pages/contact", BaseService.getHeaders())
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    when {
                        it.startsWith("mailto:") -> {
                            startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse(it)))
                        }
                        it.startsWith("tel:") -> {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                            startActivity(intent)
                        }
                        it.startsWith("http") -> {
                            openBrowser(it)
                        }
                    }
                }
                return true
            }
        }
    }

    override fun onClick(v: View?) {

    }
}