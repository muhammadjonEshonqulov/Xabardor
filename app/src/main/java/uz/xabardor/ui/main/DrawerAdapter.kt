package uz.xabardor.ui.main

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.R
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.rubric.RubricsData
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class DrawerAdapter(recyclerView: RecyclerView) :
    BaseRecyclerViewAdapter<RubricsData, DrawerAdapter.DrawerHolder>(recyclerView) {
    lateinit var language: Language

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerHolder {
        return DrawerHolder(parent)
    }

    inner class DrawerHolder(parent: ViewGroup) :
        BaseRecyclerViewHolder<RubricsData>(parent, R.layout.item_drawer_recycler_view) {

        val textView: TextView = itemView.findViewById(R.id.text_view)

        override fun bind(elem: RubricsData, position: Int) {
            if (language.id == Uzbek().id){
                textView.setText(elem.title?.capitalize())
            } else if (language.id == Krill().id){
                textView.setText(elem.title_cyrl?.capitalize())
            }
        }
    }
}