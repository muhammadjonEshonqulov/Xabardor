package uz.tima.xabardor.ui.news.content

import uz.tima.xabardor.rest.models.content.Content

interface ContentAdapterCallBack {

    fun onItemClick(content: Content)
}
