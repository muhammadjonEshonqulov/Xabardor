package uz.xabardor.ui.news.favourites

import moxy.InjectViewState
import uz.xabardor.database.NewsDatabase
import uz.xabardor.ui.base.BasePresenter
import uz.xabardor.rest.models.news.News

@InjectViewState
class FavouritesPresenter : BasePresenter<FavouritesView>() {


    fun getFavourites() {
        viewState.onSuccessFavourites(NewsDatabase.favourites)
    }

}