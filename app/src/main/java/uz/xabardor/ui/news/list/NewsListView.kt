package uz.xabardor.ui.news.list

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NewsListView: MvpView {

    fun onLoadingNewsList()

    fun onErrorNewsList(throwable: Throwable)

    fun onSuccessNewsList(next: Long?, totalPages: Long?)

}