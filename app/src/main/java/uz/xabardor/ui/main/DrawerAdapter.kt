package uz.xabardor.ui.main

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.R
import uz.xabardor.rest.models.Tag
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class DrawerAdapter(recyclerView: RecyclerView) :
    BaseRecyclerViewAdapter<Tag, DrawerAdapter.DrawerHolder>(recyclerView) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerHolder {
        return DrawerHolder(parent)
    }

    inner class DrawerHolder(parent: ViewGroup) :
        BaseRecyclerViewHolder<Tag>(parent, R.layout.item_drawer_recycler_view) {

        val textView: TextView = itemView.findViewById(R.id.text_view)

        override fun bind(elem: Tag, position: Int) {
            textView.setText(elem.title)
        }
    }
}