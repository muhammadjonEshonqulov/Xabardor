package uz.xabardor.ui.news.content

import uz.xabardor.rest.models.content.Content

interface ContentAdapterCallBack {

    fun onItemClick(content: Content)
}
