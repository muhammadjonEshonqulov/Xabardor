package uz.tima.xabardor.ui.main

import uz.tima.xabardor.rest.models.WeathersAppResponse
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.tima.xabardor.rest.models.ExchangeRatesData
import uz.tima.xabardor.rest.models.news.News

@StateStrategyType(OneExecutionStateStrategy::class)
interface MainView : MvpView {

    fun onLoadingNewsList()

    fun onErrorNewsList(throwable: Throwable)

    fun onSuccessNewsList(tag:String? = null)

    fun onSuccessTopWeatherAndExchanges(list: List<ExchangeRatesData>, list1: WeathersAppResponse)

    fun onSuccessMore(data: List<News>, groupPosition: Int)

}