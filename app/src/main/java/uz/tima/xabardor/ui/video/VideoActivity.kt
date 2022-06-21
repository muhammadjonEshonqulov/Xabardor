package uz.tima.xabardor.ui.video

import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import uz.tima.xabardor.R
import uz.tima.xabardor.ui.base.BaseActivity

class VideoActivity : BaseActivity(), VideoFullScreenActivity.OnFinishListener {
    
    private var url_video = ""
    lateinit var player: SimpleExoPlayer
    var landscape = false
    lateinit var videoActionbar : LinearLayout
    lateinit var fullscreen : ImageView
    lateinit var backToVideo : ImageView
    lateinit var playerView : PlayerView

    override fun onCreatedView() {

        videoActionbar  = findViewById(R.id.video_actionbar)
        fullscreen  = findViewById(R.id.fullscreen_icon)
        backToVideo  = findViewById(R.id.back_to_video)
        playerView  = findViewById(R.id.player_view)

        window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = Color.TRANSPARENT
        }
        //  bindingPlayer = VideoPlayerControllerBinding.bind(LayoutInflater.from(this).inflate(R.layout.video_player_controller,null,false))
         val view2 = LayoutInflater.from(this).inflate(R.layout.video_player_controller,null,false)
//         val view2 = binding.root.findViewById<View>(R.id.player_view)
        player = ExoPlayerFactory.newSimpleInstance(this)
        url_video = intent?.extras?.getString("url_video").toString()
        videoActionbar.visibility = View.GONE

        playerView.player = player
        
//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

            val dataSource = DefaultDataSourceFactory(this, Util.getUserAgent(this, "InTalim"))
            val mediaSource = ExtractorMediaSource.Factory(dataSource).setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(Uri.parse(url_video))
            player.prepare(mediaSource, true, false)

        playerView.useController
        playerView.controllerHideOnTouch
        backToVideo.setOnClickListener {
            finish()
        }
        fullscreen.setOnClickListener {
            
            if (landscape){
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                landscape = false
                fullscreen.setImageResource(R.drawable.ic_fullscreen_on)
            } else {
                this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                landscape = true
                fullscreen.setImageResource(R.drawable.ic_fullscrern_off)
            }
        }
        
    }

    override fun onFullScreenFinish() {
        playerView.player = player
    }

    override val layoutId: Int
        get() = R.layout.activity_video

    override fun onResume() {
        super.onResume()
        videoActionbar.visibility = View.GONE
//        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        player.playWhenReady = true
    }

    override fun setupToolbar() {
        toolBarTitleTextView?.setText(R.string.contact_us)
    }

    override fun onPause() {
        super.onPause()
        player.playWhenReady = false
    }
    
    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}
