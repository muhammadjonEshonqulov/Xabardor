package uz.xabardor.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
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
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.rest.models.rubric.RubricsData
import uz.xabardor.rest.services.BaseService
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.group.BaseGroupRecyclerViewHolder
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup

class MainAdapter(recyclerView: RecyclerView) :
        BaseGroupRecyclerViewAdapter<News, BaseGroupRecyclerViewHolder<News>>(recyclerView) {

    var onTagClickListener: OnTagClickListener? = null
    var onBannerOpenClickListener: OnBannerOpenClickListener? = null
    var onMoreClickListener:OnMoreClickListener? = null
    var adsenseTop : List<Adsense>? = null
    var adsenseCenter : List<Adsense>? = null
    var adsenseAfterTrendList : List<Adsense>? = null
    lateinit var language:Language
    var selected = ""

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
                VIEW_TYPE_LITTLE -> LargeHolder(parent)
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
            val more_list_item = itemView.findViewById<CardView>(R.id.more_list_item)

            if ((position + 1) % group.items.size == 0 && groupPosition != 0 && group.next != null && selected == ""){
                if (groups.size == 4){
                    more_list_item.visibility = View.VISIBLE
                    more_list_item.setOnClickListener {
                        onMoreClickListener?.onMoreClick(groupPosition)
                    }
                } else {
                    if (groupPosition != 2){
                        more_list_item.visibility = View.VISIBLE
                        more_list_item.setOnClickListener {
                            onMoreClickListener?.onMoreClick(groupPosition)
                        }
                    } else{
                        more_list_item.visibility = View.GONE
                    }
                }

            } else {
                more_list_item.visibility = View.GONE
            }

            val glide = Glide.with(recyclerView.context)
            glide.clear(imageView)
            glide
                    .load(elem.image.thumb)
                    .into(imageView)
            if (language.id == Krill().id){
                elem.title_cyrl?.let {
                    titleTextView.setText(it)
                }
                elem.description_cyrl?.let {
                    descriptionTextView.setText(it)
                }
            } else if (language.id == Uzbek().id){
                elem.title?.let {
                    titleTextView.setText(it)
                }
                elem.description?.let {
                    descriptionTextView.setText(it)
                }
            }


            elem.tags?.let { tags ->
                tagContainerLayout.setTags(tags.map {
                    if (language.id == Uzbek().id){
                        it.title?.let {
                            "#${it}"
                        }
                    } else {
                        it.title_cyrl?.let {
                            "#${it}"
                        }
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

        override fun getHeaderView(group: RecyclerViewGroup<News>, groupPosition: Int): View {

                if (groupPosition == 2 && group.items.size == 10) {
                val header = LayoutInflater.from(itemView.context).inflate(R.layout.header_actual, null, false)
                header.findViewById<TextView>(R.id.title_header).text = header.context.getString(R.string.last_news)
                return header
            }
            else if (groupPosition != 0) {
                val view = View(itemView.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 24.dpToPx())
                view.setBackgroundResource(R.color.colorPrimary)
                return view
            }

            return super.getHeaderView(group, groupPosition)
        }
    }

    inner class LittleHolder(parent: ViewGroup) : BaseGroupRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_little) {

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

//        val webView: WebView = itemView.findViewById(R.id.web_view)
        val textView: TextView = itemView.findViewById(R.id.adsense_text_view_title)
        val adsense_image_view_top: ImageView = itemView.findViewById(R.id.adsense_image_view_top)
        val adsense_image_view_center: ImageView = itemView.findViewById(R.id.adsense_image_view_center)
        val adsense_image_view_bottom: ImageView = itemView.findViewById(R.id.adsense_image_view_bottom)
        val adsense_item_back: LinearLayout = itemView.findViewById(R.id.adsense_item_back)

        init {

//            val width = itemView.context.resources.displayMetrics.widthPixels

//            webView.layoutParams.height = Math.ceil(width / 2.0).toInt()
//            webView.settings.javaScriptEnabled = true
//            webView.webViewClient = object: WebViewClient(){
//                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                    url?.let {
//                        onBannerOpenClickListener?.onOpenBanner(it)
//                    }
//                    return true
//                }
//            }
        }

        override fun bind(elem: News, group: RecyclerViewGroup<News>, groupPosition: Int, position: Int) {
            if (groupPosition == 1 && groups.size > 3){

                val glide = Glide.with(recyclerView.context)
                adsense_image_view_top.adjustViewBounds = true
                adsense_image_view_top.visibility = View.VISIBLE
                adsense_image_view_center.visibility = View.GONE
                adsense_image_view_bottom.visibility = View.GONE
                glide.clear(adsense_image_view_top)
                glide
                    .load(adsenseTop?.get(0)?.photo)
                    .into(adsense_image_view_top)
            } else if (groupPosition == 3 && groups.size > 3){
                val glide = Glide.with(recyclerView.context)
                adsense_image_view_center.adjustViewBounds = true
                adsense_image_view_top.visibility = View.GONE
                adsense_image_view_center.visibility = View.VISIBLE
                adsense_image_view_bottom.visibility = View.GONE
                glide.clear(adsense_image_view_center)
                glide
                    .load(adsenseCenter?.get(0)?.photo)
                    .into(adsense_image_view_center)
            }
            else if (groupPosition == 5){
                val glide = Glide.with(recyclerView.context)
                adsense_image_view_center.adjustViewBounds = true
                adsense_image_view_top.visibility = View.GONE
                adsense_image_view_center.visibility = View.GONE
                adsense_image_view_bottom.visibility = View.VISIBLE
                glide.clear(adsense_image_view_bottom)
                glide
                    .load(adsenseAfterTrendList?.get(0)?.photo)
                    .into(adsense_image_view_bottom)
            } else {
                val glide = Glide.with(recyclerView.context)
                adsense_image_view_top.adjustViewBounds = true
                adsense_image_view_top.visibility = View.VISIBLE
                adsense_image_view_center.visibility = View.GONE
                adsense_image_view_bottom.visibility = View.GONE
                glide.clear(adsense_image_view_top)
                glide
                    .load(adsenseTop?.get(0)?.photo)
                    .into(adsense_image_view_top)
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
//                webView.loadUrl(BaseService.BASE_API_URL + "ad/center", headers)
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
interface OnMoreClickListener {
    fun onMoreClick(groupPosition: Int)
}

interface OnBannerOpenClickListener {
    fun onOpenBanner(url: String)
}