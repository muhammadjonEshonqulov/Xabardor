package uz.xabardor.ui.news.content.viewholders

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import uz.xabardor.rest.models.content.Content

abstract class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var listener: ((Content) -> Unit)? = null

    abstract fun bindData(data: Content)
}