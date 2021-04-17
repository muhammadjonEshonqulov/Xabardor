package uz.xabardor.ui.news

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface NewsView : MvpView {

    fun onLoadingNewsDetail()

    fun onErrorNewsDetail()

    fun onSuccessNewsDetail()

}