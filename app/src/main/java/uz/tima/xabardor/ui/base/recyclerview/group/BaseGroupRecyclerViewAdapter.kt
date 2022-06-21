package uz.tima.xabardor.ui.base.recyclerview.group

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import uz.tima.xabardor.ui.base.recyclerview.OnBottomScrolledListener
import uz.tima.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroup
import uz.tima.xabardor.ui.base.recyclerview.group.model.RecyclerViewGroupItem

abstract class BaseGroupRecyclerViewAdapter<T, VH : BaseGroupRecyclerViewHolder<T>>(val recyclerView: RecyclerView) : RecyclerView.Adapter<VH>() {

    init {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = this
    }

    var onBottomScrolledListener: OnBottomScrolledListener? = null
    var onGroupRecyclerViewItemClickListener: OnGroupRecyclerViewItemClickListener<T>? = null

    var groups = listOf<RecyclerViewGroup<T>>()
    var items = listOf<RecyclerViewGroupItem<T>>()


    final override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return getItemViewType(
                elem = item.elem,
                group = item.group,
                groupPosition = item.groupPosition,
                position = item.position
        )
    }

    open fun getItemViewType(elem: T, group: RecyclerViewGroup<T>, groupPosition: Int, position: Int): Int {
        return 1
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.bindView(
                elem = item.elem,
                group = item.group,
                groupPosition = item.groupPosition,
                position = item.position
        )

        holder.itemView.setOnClickListener {
            onGroupRecyclerViewItemClickListener?.onItemClick(
                    recyclerView = recyclerView,
                    elem = item.elem,
                    group = item.group,
                    groupPosition = item.groupPosition,
                    position = item.position
            )
        }

        if (position == items.size - 1)
            onBottomScrolledListener?.onBottomScrolled(recyclerView)
    }


    fun onSuccess(groups: List<RecyclerViewGroup<T>>) {
        this.groups = groups
        this.items = listOf()
        if (groups.isNotEmpty()){
            groups.forEachIndexed { groupIndex, group ->
                group.items.forEachIndexed { index, groupItem ->
                    val item = RecyclerViewGroupItem<T>(
                        isHeader = index == 0,
                        elem = groupItem,
                        group = group,
                        groupPosition = groupIndex,
                        position = index
                    )

                    this.items = this.items.plus(item)
                }
            }

        } else {
            this.items = listOf()
        }

        notifyDataSetChanged()
    }


}