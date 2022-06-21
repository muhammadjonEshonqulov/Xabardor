package uz.tima.xabardor.ui.base.recyclerview.group

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup

abstract class BaseGroupRecyclerViewHolder<T>(parent: ViewGroup, val layoutId: Int) :
        RecyclerView.ViewHolder(
                LinearLayout(parent.context).apply {
                    orientation = LinearLayout.VERTICAL
                }
        ) {

    private val headerParentView: RelativeLayout

    init {
        headerParentView = RelativeLayout(parent.context)
        headerParentView.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

        (itemView as? LinearLayout)?.let {
            it.addView(headerParentView)

            val contentView = LayoutInflater.from(parent.context).inflate(layoutId, itemView, false)
            contentView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            it.addView(contentView)
        }
    }


    fun bindView(elem: T, group: RecyclerViewGroup<T>, groupPosition: Int, position: Int) {
        headerParentView.removeAllViews()
        if (position == 0)
            headerParentView.addView(getHeaderView(group, groupPosition))

        bind(elem = elem, group = group, groupPosition = groupPosition, position = position)
    }

    abstract protected fun bind(elem: T, group: RecyclerViewGroup<T>, groupPosition: Int, position: Int)

    open fun getHeaderView(group: RecyclerViewGroup<T>, groupPosition: Int): View {
        return View(itemView.context)
    }

}