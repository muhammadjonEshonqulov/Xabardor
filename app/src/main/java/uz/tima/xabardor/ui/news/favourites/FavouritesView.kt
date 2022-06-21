package uz.tima.xabardor.ui.news.favourites

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.tima.xabardor.rest.models.news.News

@StateStrategyType(OneExecutionStateStrategy::class)
interface FavouritesView : MvpView {

    fun onSuccessFavourites(list: List<News>)

}