package uz.tima.xabardor.ui.base.recyclerview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T, VH : BaseRecyclerViewHolder<T>>(
        val recyclerView: RecyclerView
) : RecyclerView.Adapter<VH>() {

    init {
        recyclerView.layoutManager = LinearLayoutManager(recyclerView.context)
        recyclerView.adapter = this
    }

    var onBottomScrolledListener: OnBottomScrolledListener? = null
    var onItemClickListener: OnItemClickListener<T>? = null
    var items = listOf<T>()

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position], position)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(recyclerView, items[position], position)
        }

        if (position == items.size - 1) {
            onBottomScrolledListener?.onBottomScrolled(recyclerView)
        }
    }

    fun onSuccess(list: List<T>) {
        items = list
        notifyDataSetChanged()
    }
}

interface   OnItemClickListener<T> {
    fun onItemClick(recyclerView: RecyclerView, item: T, position: Int)
}

interface OnBottomScrolledListener {
    fun onBottomScrolled(recyclerView: RecyclerView)
}