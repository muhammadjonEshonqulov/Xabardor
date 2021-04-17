package uz.xabardor.ui.news

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.R
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class RelatedAdapter(recyclerView: RecyclerView) :
    BaseRecyclerViewAdapter<News, RelatedAdapter.RelatedHolder>(recyclerView) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RelatedHolder(parent)

    inner class RelatedHolder(parent: ViewGroup) :
        BaseRecyclerViewHolder<News>(parent, R.layout.item_recyclerview_news_related) {

        var titleTextView: TextView = itemView.findViewById(R.id.text_view_title)
        var tagTextView: TextView = itemView.findViewById(R.id.text_view_tag)

        override fun bind(elem: News, position: Int) {
            titleTextView.setText(elem.title)

            elem.tags?.firstOrNull()?.let {
                tagTextView.setText(it.title)
            } ?: run {
                tagTextView.setText("")
            }
        }
    }
}