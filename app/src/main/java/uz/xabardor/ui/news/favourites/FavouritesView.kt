package uz.xabardor.ui.news.favourites

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import uz.xabardor.rest.models.news.News

@StateStrategyType(OneExecutionStateStrategy::class)
interface FavouritesView : MvpView {

    fun onSuccessFavourites(list: List<News>)

}