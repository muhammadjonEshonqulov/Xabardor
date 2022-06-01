package uz.xabardor.ui.main

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.R
import uz.xabardor.extensions.language.Krill
import uz.xabardor.extensions.language.Language
import uz.xabardor.extensions.language.Uzbek
import uz.xabardor.extensions.lg
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.rubric.RubricsData
import uz.xabardor.ui.base.ToggleAnimation
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewAdapter
import uz.xabardor.ui.base.recyclerview.BaseRecyclerViewHolder

class DrawerAdapter(recyclerView: RecyclerView) :
    BaseRecyclerViewAdapter<RubricsData, DrawerAdapter.DrawerHolder>(recyclerView) {
    lateinit var language: Language
    lateinit var onItemClickListenerr: OnItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrawerHolder {
        return DrawerHolder(parent)
    }
    interface OnItemClickListener {
        fun onItemClick(position: Int, data: RubricsData)
    }
    inner class DrawerHolder(parent: ViewGroup) :
        BaseRecyclerViewHolder<RubricsData>(parent, R.layout.item_drawer_recycler_view) {

        val textView: TextView = itemView.findViewById(R.id.text_view)
        val more_topic_item: ImageView = itemView.findViewById(R.id.more_topic_item)
        val rubric_item: LinearLayout = itemView.findViewById(R.id.rubric_item)
        val rubrics_list: RecyclerView = itemView.findViewById(R.id.rubrics_list)

        override fun bind(elem: RubricsData, position: Int) {

            if(elem.expandable){
                rubrics_list.visibility = View.VISIBLE
                ToggleAnimation.toggleArrow(more_topic_item, true)
            } else {
                rubrics_list.visibility = View.GONE
                ToggleAnimation.toggleArrow(more_topic_item, false)
            }
            if (elem.rubrics == null){
                more_topic_item.visibility = View.GONE
            } else {
                more_topic_item.visibility = View.VISIBLE
            }
            val rubricsAdapter = RubricsAdapter()
            rubricsAdapter.language = language
            rubrics_list.adapter = rubricsAdapter
            rubrics_list.layoutManager = LinearLayoutManager(itemView.context)
            lg("elem.rubrics"+elem)
            elem.rubrics?.let {
                rubricsAdapter.data.clear()
                rubricsAdapter.data.addAll(it)
            }
            rubricsAdapter.notifyDataSetChanged()
            rubricsAdapter.setOnClickListener {
                onItemClickListenerr.onItemClick(-1, it)
            }
            rubric_item.setOnClickListener {
                onItemClickListenerr.onItemClick(-1, elem)
                elem.expandable = !elem.expandable
            }
            more_topic_item.setOnClickListener {
                onItemClickListenerr.onItemClick(bindingAdapterPosition, elem)
                elem.expandable = !elem.expandable
            }

            if (language.id == Uzbek().id){
                textView.setText(elem.title?.capitalize())
            } else if (language.id == Krill().id){
                textView.setText(elem.title_cyrl?.capitalize())
            }
        }
    }
}