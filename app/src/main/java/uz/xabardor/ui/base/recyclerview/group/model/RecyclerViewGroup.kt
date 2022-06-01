package uz.xabardor.ui.base.recyclerview.group.model

import java.io.Serializable

class RecyclerViewGroup<T>(
    var items: List<T>,
    var next: Long? = null
) : Serializable