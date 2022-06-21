package uz.tima.xabardor.ui.news.favourites

import moxy.InjectViewState
import uz.tima.xabardor.database.NewsDatabase
import uz.tima.xabardor.ui.base.BasePresenter
import uz.tima.xabardor.rest.models.news.News

@InjectViewState
class FavouritesPresenter : BasePresenter<FavouritesView>() {


    fun getFavourites() {
        viewState.onSuccessFavourites(NewsDatabase.favourites)
    }

}