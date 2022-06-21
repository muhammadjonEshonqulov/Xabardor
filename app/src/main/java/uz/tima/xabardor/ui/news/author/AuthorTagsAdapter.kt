package uz.tima.xabardor.ui.news.author

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.R
import uz.tima.xabardor.rest.models.Tag
import uz.tima.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.tima.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class AuthorTagsAdapter(recyclerView: RecyclerView) : BaseRecyclerViewAdapter<Tag, AuthorTagsAdapter.TagHolder>(recyclerView) {

    var selectedTag: Tag? = null

    init {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context).apply {
            orientation = LinearLayoutManager.HORIZONTAL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
        return TagHolder(parent)
    }


    inner class TagHolder(parent: ViewGroup) : BaseRecyclerViewHolder<Tag>(parent, R.layout.item_recyclerview_tag_author) {

        val textView: TextView = itemView.findViewById(R.id.text_view)

        override fun bind(elem: Tag, position: Int) {
            if (position == 0)
                textView.setText(R.string.all)
            else
                textView.setText(elem.title)


            if (selectedTag?.id == elem.id) {
                textView.setTextColor(itemView.resources.getColor(R.color.colorTextBlue))
            } else {
                textView.setTextColor(itemView.resources.getColor(R.color.colorBlack))
            }
        }
    }
}