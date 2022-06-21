package uz.tima.xabardor.ui.splash

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SplashView: MvpView {

    fun onLoadingTags()

    fun onErrorTags(throwable: Throwable)

    fun onSuccessTags()

}