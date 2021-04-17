package uz.xabardor.ui.main

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView: MvpView {

    fun onLoadingNewsList()

    fun onErrorNewsList(throwable: Throwable)

    fun onSuccessNewsList()

}