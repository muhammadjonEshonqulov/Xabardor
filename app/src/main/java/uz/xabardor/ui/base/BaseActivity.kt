package uz.xabardor.ui.base

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import moxy.MvpAppCompatActivity
import uz.xabardor.R
import uz.xabardor.extensions.openNoConnectionActivity
import uz.xabardor.rest.services.BaseService
import uz.xabardor.rest.services.NoConnectionListener

abstract class BaseActivity : MvpAppCompatActivity(), View.OnClickListener, NoConnectionListener {

    var toolBar: Toolbar? = null
    var toolBarLogoImageView: ImageView? = null
    var toolBarMenuImageView: ImageView? = null
    var toolBarBackImageView: ImageView? = null
    var toolBarSearchImageView: ImageView? = null
    var toolBarFavouriteImageView: ImageView? = null
    var toolBarShareImageView: ImageView? = null

    var toolBarSearchEditText: EditText? = null

    var toolBarTitleTextView: TextView? = null

    abstract val layoutId: Int get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)

        toolBar = findViewById(R.id.tool_bar)
        setSupportActionBar(toolBar)

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


}