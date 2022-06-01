package uz.xabardor.ui.news.content.viewholders

import android.view.View
import androidx.fragment.app.FragmentActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import uz.xabardor.R
import uz.xabardor.rest.models.content.Content


class YouTubeVideoViewHolder(val view: View) :
    BaseViewHolder(view) {
    private lateinit var data: Content

    override fun bindData(data: Content) {
        this.data = data


        val youTubePlayerView = view.findViewById<YouTubePlayerView>(R.id.youtube_video)  // view.findViewById<FrameLayout>(R.id.youtube_video)

        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.cueVideo(""+data.src?.split("embed/")?.last(), 0f)
            }
        })
    }

}