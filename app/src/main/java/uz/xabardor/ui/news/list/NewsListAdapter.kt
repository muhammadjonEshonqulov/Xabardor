package uz.xabardor.ui.news.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.get
import androidx.recyclerview.widget.RecyclerView
import co.lujun.androidtagview.TagContainerLayout
import co.lujun.androidtagview.TagView
import com.bumptech.glide.Glide
import uz.xabardor.R
import uz.xabardor.extensions.dpToPx
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.extensions.lg
import uz.xabardor.rest.models.Adsense
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.services.BaseService
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewHolder
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup
import uz.xabardor.ui.main.OnBannerOpenClickListener
import uz.xabardor.ui.main.OnTagClickListener

class NewsListAdapter(reclerView: RecyclerView) :
    BaseGroupRecyclerViewAdapter<News, BaseGroupRecyclerViewHolder<News>>(reclerView) {

    var onTagClickListener: OnTagClickListener? = null
    var onBannerOpenClickListener: OnBannerOpenClickListener? = null

    var adsenseTop : List<Adsense>? = null
    var adsenseCenter : List<Adsense>? = null
    lateinit var language: Language

    override fun getItemViewType(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int): Int {
        when {
            groupPosition == 0 -> {
                return VIEW_TYPE_LARGE
            }

            groupPosition == 1 || groupPosition == 3 -> {
                return VIEW_TYPE_BANNER
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
            VIEW_TYPE_LITTLE -> LargeHolder(parent)
            VIEW_TYPE_BANNER -> BannerHolder(parent)
            else -> LargeHolder(parent)
        }

    inner class LargeHolder(parent: ViewGroup) :
        BaseGroupRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_large) {

        val tagContainerLayout: TagContainerLayout = itemView.findViewById(R.id.tag_container_layout)
        val imageView: ImageView = itemView.findViewById(R.id.image_view)

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        var descriptionTextView: TextView = itemView.findViewById(R.id.text_view_description)

        init {
            val width = itemView.context.resources.displayMetrics.widthPixels
            imageView.layoutParams.height = width * 180 / 320
        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {
            if (language.id == Krill().id){
                titleTextView.setText(elem.title_cyrl)
                descriptionTextView.setText(elem.description_cyrl)
            } else if (language.id == Uzbek().id){
                titleTextView.setText(elem.title)
                descriptionTextView.setText(elem.description)
            }



            val glide = Glide.with(recyclerView.context)
            glide.clear(imageView)
            glide
                .load(elem.image.thumb)
                .into(imageView)


            elem.tags?.let { tags ->
                tagContainerLayout.setTags(tags.map {
                    if (language.id == Uzbek().id){
                        "#${it.title}"
                    } else {
                        "#${it.title_cyrl}"
                    }
                })
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
                            if (language.id == Uzbek().id){
                                "#${it.title}" == str
                            } else {
                                "#${it.title_cyrl}" == str
                            }
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

        val textView: TextView = itemView.findViewById(R.id.adsense_text_view_title)
        val imageView: ImageView = itemView.findViewById(R.id.adsense_image_view)
        val adsense_item_back: LinearLayout = itemView.findViewById(R.id.adsense_item_back)

        init {
//            val width = itemView.context.resources.displayMetrics.widthPixels
//            webView.layoutParams.height = Math.ceil(width / 2.0).toInt()
//            webView.settings.javaScriptEnabled = true
//            webView.webViewClient = object : WebViewClient() {
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    url?.let {
//                        onBannerOpenClickListener?.onOpenBanner(it)
//                    }
//                    return true
//                }
//            }
        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {
            val headers = BaseService.getHeaders()
            if (elem.isTopBanner){
                val glide = Glide.with(recyclerView.context)
                glide.clear(imageView)
                glide
                    .load(adsenseTop?.get(0)?.photo)
                    .into(imageView)
//                adsenseTop?.get(0)?.title?.let {
//                    textView.setText(it)
//                }
            }
//                webView.loadUrl(BaseService.BASE_API_URL + "ad/top", headers).javaClass
            else {
                val glide = Glide.with(recyclerView.context)
                glide.clear(imageView)
                glide
                    .load(adsenseCenter?.get(0)?.photo)
                    .into(imageView)
//                adsenseCenter?.get(0)?.title?.let {
//                    textView .setText(it)
//                }

            }
            adsense_item_back.setOnClickListener {
                if (elem.isTopBanner){
                    adsenseTop?.get(0)?.link?.let {
                        onBannerOpenClickListener?.onOpenBanner(it)
                    }
                } else {
                    adsenseCenter?.get(0)?.link?.let {
                        onBannerOpenClickListener?.onOpenBanner(it)
                    }
                }
            }
        }
    }

    companion object {
        val VIEW_TYPE_LARGE = 1
        val VIEW_TYPE_LITTLE = 2
        val VIEW_TYPE_BANNER = 3
    }
}