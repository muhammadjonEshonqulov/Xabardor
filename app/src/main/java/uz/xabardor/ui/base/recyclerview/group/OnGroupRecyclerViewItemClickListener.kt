package uz.xabardor.ui.base.recyclerview.group

import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup

interface OnGroupRecyclerViewItemClickListener<T> {
    fun onItemClick(recyclerView: RecyclerView, elem: T, group: RecyclerViewGroup<T>, groupPosition: Int, position: Int)
}