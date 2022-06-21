package uz.tima.xabardor.ui.about

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.tima.xabardor.rest.models.AboutResponse

@StateStrategyType(OneExecutionStateStrategy::class)
interface AboutUsView:MvpView {
    fun onErrorNewsList(throwable: Throwable)
    fun onSuccessNewsList(elem: List<AboutResponse>)
}