package uz.tima.xabardor.ui.news

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.Krill
import uz.tima.xabardor.extensions.language.Language
import uz.tima.xabardor.extensions.language.Uzbek
import uz.tima.xabardor.rest.models.news.News
import uz.tima.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.tima.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class RelatedAdapter(recyclerView: RecyclerView) :
    BaseRecyclerViewAdapter<News, RelatedAdapter.RelatedHolder>(recyclerView) {

    lateinit var language: Language
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RelatedHolder(parent)

    inner class RelatedHolder(parent: ViewGroup) :
        BaseRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_related) {

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        var tagTextView: TextView = itemView.findViewById(R.id.text_view_tag)

        override fun bind(elem: News, position: Int) {
            Language.getNameByLanguage(elem.tags?.firstOrNull()?.title, elem.tags?.firstOrNull()?.title_cyrl,language)?.let {
                tagTextView.setText(""+it)
            }
            Language.getNameByLanguage(elem.title, elem.title_cyrl, language)?.let {
                titleTextView.setText(""+it)
            }


//            elem.tags?.firstOrNull()?.let {
//                if (language.id == Krill().id){
//                    tagTextView.setText(it.title_cyrl)
//                } else if (language.id == Uzbek().id){
//                    tagTextView.setText(it.title)
//                }
//            } ?: run {
//                tagTextView.setText("")
//            }
        }
    }
}