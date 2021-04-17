package uz.xabardor.ui.main

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.bumptech.glide.Glide
import uz.xabardor.R
import uz.xabardor.extensions.dpToPx
import uz.xabardor.extensions.openBrowser
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.BaseService
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewHolder
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup

class MainAdapter(recyclerView: RecyclerView) :
        BaseGroupRecyclerViewAdapter<News, BaseGroupRecyclerViewHolder<News>>(recyclerView) {

    var onTagClickListener: OnTagClickListener? = null
    var onBannerOpenClickListener: OnBannerOpenClickListener? = null


    override fun getItemViewType(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int): Int {
        when {
            groupPosition == 0 -> {
                return VIEW_TYPE_LARGE
            }

            groupPosition == 1 || groupPosition == 3 -> {
                return VIEW_TYPE_BANNER
            }

            groupPosition == 2 -> {
                return VIEW_TYPE_LITTLE
            }

            groupPosition == 4 -> {
                if (elem.image.showInList)
                    return VIEW_TYPE_LARGE
                else
                    return VIEW_TYPE_LITTLE
            }
            else -> {
                if (elem.image.showInList) {
                    return VIEW_TYPE_LARGE
                }
                return VIEW_TYPE_LITTLE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseGroupRecyclerViewHolder<News> =
            when (viewType) {
                VIEW_TYPE_LARGE -> LargeHolder(parent)
                VIEW_TYPE_LITTLE -> LittleHolder(parent)
                VIEW_TYPE_BANNER -> BannerHolder(parent)
                else -> LargeHolder(parent)
            }


    inner class LargeHolder(parent: ViewGroup) :
            BaseGroupRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_large) {

        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val tagContainerLayout: TagContainerLayout = itemView.findViewById(R.id.tag_container_layout)

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        var descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)

        init {
            val width = itemView.context.resources.displayMetrics.widthPixels
            imageView.layoutParams.height = width * 180 / 320

        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {
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

        override fun getHeaderView(group: RecyclerViewGroup<News>, groupPosition: Int): View {
            if (groupPosition == 4) {
                val header = LayoutInflater.from(itemView.context).inflate(R.layout.header_actual, null, false)
                return header
            } else if (groupPosition != 0) {
                val view = View(itemView.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 24.dpToPx())
                view.setBackgroundResource(R.color.colorPrimary)
                return view
            }

            return super.getHeaderView(group, groupPosition)
        }
    }

    inner class LittleHolder(parent: ViewGroup) :
            BaseGroupRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_little) {

        val tagContainerLayout: TagContainerLayout = itemView.findViewById(R.id.tag_container_layout)

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)

        init {
//            tagContainerLayout.setOnTagClickListener(onTagClickListener)
        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {

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

        override fun getHeaderView(group: RecyclerViewGroup<News>, groupPosition: Int): View {
            if (groupPosition == 1) {
                val header = LayoutInflater.from(itemView.context).inflate(R.layout.header_actual, null, false)
                return header
            } else if (groupPosition != 2) {
                val view = View(itemView.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 24.dpToPx())
                view.setBackgroundResource(R.color.colorPrimary)
                return view
            }
            return super.getHeaderView(group, groupPosition)
        }

    }


    inner class BannerHolder(parent: ViewGroup) :
            BaseGroupRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_banner) {

        val webView: WebView = itemView.findViewById(R.id.web_view)

        init {
            val width = itemView.context.resources.displayMetrics.widthPixels
            webView.layoutParams.height = Math.ceil(width / 2.0).toInt()
            webView.settings.javaScriptEnabled = true
            webView.webViewClient = object: WebViewClient(){
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        onBannerOpenClickListener?.onOpenBanner(it)
                    }
                    return true
                }
            }
        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {
            val headers = BaseService.getHeaders()
            if (elem.isTopBanner)
                webView.loadUrl(BaseService.BASE_API_URL + "ad/top", headers)
            else
                webView.loadUrl(BaseService.BASE_API_URL + "ad/center", headers)
        }
    }


    companion object {
        val VIEW_TYPE_LARGE = 1
        val VIEW_TYPE_LITTLE = 2
        val VIEW_TYPE_BANNER = 3
    }
}

interface OnTagClickListener {
    fun onTagClick(tag: Tag)
}

interface OnBannerOpenClickListener {
    fun onOpenBanner(url: String)
}