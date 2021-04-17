package uz.xabardor.ui.news.author

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface AuthorView : MvpView {

    fun onLoadingAuthorInfo()

    fun onErrorAuthorInfo(throwable: Throwable)

    fun onSuccessAuthorInfo()


    fun onLoadingAuthorNewsList()

    fun onErrorAuthorNewsList(throwable: Throwable)

    fun onSuccessAuthorNewsList()

}