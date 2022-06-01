package uz.xabardor.ui.video

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import uz.xabardor.R

class VideoFullScreenActivity : AppCompatActivity() {
    val fullscreenIcon = findViewById<ImageView>(R.id.fullscreen_icon)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_full_screen)

        player?.let {
            val playerView = findViewById<PlayerView>(R.id.player_view)
            playerView.player = it
        }
        val videoActionbar = findViewById<LinearLayout>(R.id.video_actionbar)
        val backToVideo = findViewById<ImageView>(R.id.back_to_video)
        val videoNameTxt = findViewById<TextView>(R.id.video_name_txt)

        videoActionbar.visibility = View.VISIBLE

        backToVideo.setOnClickListener {
            finish()
        }
        videoNameTxt.text = "Video name"
        fullscreenIcon.setOnClickListener { finish() }
        
    }
    override fun onResume() {
        super.onResume()
        player?.playWhenReady = true
        fullscreenIcon.setImageResource(R.drawable.ic_fullscrern_off)
    }
    
    override fun onPause() {
        super.onPause()
        player?.playWhenReady = false
    }
    
    companion object {
        var player: SimpleExoPlayer? = null
        var listener: OnFinishListener? = null
    }
    
    interface OnFinishListener {
        fun onFullScreenFinish()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        listener?.onFullScreenFinish()
    }
    
    
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
    }
}