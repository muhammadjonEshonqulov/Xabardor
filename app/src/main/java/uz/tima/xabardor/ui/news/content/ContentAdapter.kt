package uz.tima.xabardor.ui.news.content

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.LanguageManager
import uz.tima.xabardor.rest.models.content.Content
import uz.tima.xabardor.rest.models.content.ContentConstants
import uz.tima.xabardor.ui.news.content.viewholders.*

class ContentAdapter : RecyclerView.Adapter<BaseViewHolder>() {

    var data: List<Content> = ArrayList()
    var callBack: ContentAdapterCallBack? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val language = LanguageManager(parent.context).currentLanguage

        if (viewType == ContentConstants.Type.PHOTO){
            return PhotoViewHolder(inflater.inflate(R.layout.item_content_photo, parent, false))
        } else if (viewType == ContentConstants.Type.VIDEO){
            return VideoViewHolder(inflater.inflate(R.layout.item_content_video, parent, false))
        } else if (viewType == ContentConstants.Type.CAPTURE_TEXT){
            return CaptureTextViewHolder(inflater.inflate(R.layout.item_content_capture_text, parent, false))
        } else if (viewType == ContentConstants.Type.YOUTUBE_VIDEO){
            return YouTubeVideoViewHolder(inflater.inflate(R.layout.item_content_youtube_video, parent, false))
        } else {
            return TextViewHolder(inflater.inflate(R.layout.item_content_text, parent, false))
        }

    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bindData(data[position])
        holder.listener = {
            callBack?.onItemClick(it)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(data[position].type == "image") ContentConstants.Type.PHOTO else if(data[position].type == "image-caption") ContentConstants.Type.CAPTURE_TEXT else if(data[position].type == "yotube") ContentConstants.Type.YOUTUBE_VIDEO else if(data[position].type == "video") ContentConstants.Type.VIDEO else ContentConstants.Type.TEXT
    }
}