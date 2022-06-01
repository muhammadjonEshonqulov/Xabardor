package uz.xabardor.ui.news.content.viewholders

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import uz.xabardor.R
import uz.xabardor.rest.models.content.Content

class PhotoViewHolder(val view: View) :
    BaseViewHolder(view) {
    override fun bindData(data: Content) {
        val contentItemPhoto = view.findViewById<ImageView>(R.id.content_item_photo)
        Glide.with(itemView.context).load(data.src).into(contentItemPhoto)
        contentItemPhoto.setOnClickListener {
            listener?.invoke(data)
        }
    }
}

