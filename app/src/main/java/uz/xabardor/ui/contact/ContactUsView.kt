package uz.xabardor.ui.contact

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.xabardor.rest.models.AboutResponse

@StateStrategyType(OneExecutionStateStrategy::class)
interface ContactUsView : MvpView {
    fun onErrorNewsList(throwable: Throwable)
    fun onSuccessNewsList(elem: List<AboutResponse>)
}