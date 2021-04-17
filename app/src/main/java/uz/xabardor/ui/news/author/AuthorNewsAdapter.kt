package uz.xabardor.ui.news.author

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.bumptech.glide.Glide
import uz.xabardor.R
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder
import uz.xabardor.ui.main.OnTagClickListener

class AuthorNewsAdapter(reclerView: RecyclerView) :
        BaseRecyclerViewAdapter<News, BaseRecyclerViewHolder<News>>(reclerView) {

    init {
        reclerView.layoutManager?.setAutoMeasureEnabled(true);
    }

    var onTagClickListener: OnTagClickListener? = null


    override fun getItemViewType(position: Int): Int {
        if (items[position].image.showInList)
            return VIEW_TYPE_LARGE
        return VIEW_TYPE_LITTLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<News> {
        when (viewType) {
            VIEW_TYPE_LARGE -> {
                return LargeHolder(parent)
            }
            else -> {
                return LittleHolder(parent)
            }
        }
    }

    inner class LargeHolder(parent: ViewGroup) :
            BaseRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_large) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val tagContainerLayout: TagContainerLayout = itemView.findViewById(R.id.tag_container_layout)

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        var descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)

        init {
            val width = itemView.context.resources.displayMetrics.widthPixels
            imageView.layoutParams.height = width * 180 / 320

        }

        override fun bind(elem: News, position: Int) {
            val glide = Glide.with(recyclerView.context)
            glide.clear(imageView)
            glide
                    .load(elem.image.thumb)
                    .into(imageView)


            titleTextView.setText(elem.title)
            descriptionTextView.setText(elem.description)

            elem.tags?.let { tags ->
                tagContainerLayout.setTags(tags.map { "#${it.title}" })
            } ?: run {
                tagContainerLayout.removeAllTags()
            }


            tagContainerLayout.setOnTagClickListener(object : TagView.OnTagClickListener {
                override fun onSelectedTagDrag(position: Int, text: String?) {

                }

                override fun onTagLongClick(position: Int, text: String?) {

                }

                override fun onTagClick(position: Int, text: String?) {
                    text?.let { str ->
                        elem.tags?.find {
                            "#${it.title}" == str
                        }?.let {
                            onTagClickListener?.onTagClick(it)
                        }
                    }
                }

                override fun onTagCrossClick(position: Int) {

                }
            })
        }
    }

    inner class LittleHolder(parent: ViewGroup) :
            BaseRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_little) {

        val tagContainerLayout: TagContainerLayout = itemView.findViewById(R.id.tag_container_layout)

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)

        override fun bind(elem: News, position: Int) {
            titleTextView.setText(elem.title)


            elem.tags?.let { tags ->
                tagContainerLayout.setTags(tags.map { "#${it.title}" })
            } ?: run {
                tagContainerLayout.removeAllTags()
            }

            tagContainerLayout.setOnTagClickListener(object : TagView.OnTagClickListener {
                override fun onSelectedTagDrag(position: Int, text: String?) {

                }

                override fun onTagLongClick(position: Int, text: String?) {

                }

                override fun onTagClick(position: Int, text: String?) {
                    text?.let { str ->
                        elem.tags?.find {
                            "#${it.title}" == str
                        }?.let {
                            onTagClickListener?.onTagClick(it)
                        }
                    }
                }

                override fun onTagCrossClick(position: Int) {

                }
            })
        }

    }

    companion object {
        val VIEW_TYPE_LARGE = 1
        val VIEW_TYPE_LITTLE = 2
    }
}