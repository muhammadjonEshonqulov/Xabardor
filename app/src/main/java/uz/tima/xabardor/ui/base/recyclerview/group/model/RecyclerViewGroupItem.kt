package uz.tima.xabardor.ui.base.recyclerview.group.model

import java.io.Serializable

class RecyclerViewGroupItem<T>(
    val isHeader: Boolean,
    val elem: T,
    val group: RecyclerViewGroup<T>,
    val groupPosition: Int,
    val position: Int
) : Serializable