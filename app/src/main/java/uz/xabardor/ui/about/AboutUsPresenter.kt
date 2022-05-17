package uz.xabardor.ui.about

import moxy.InjectViewState
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.AboutResponse
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class AboutUsPresenter: BasePresenter<AboutUsView>() {

    fun getAbout() {
        NewsService.getAbout(
            callback = object : BaseCallback<List<AboutResponse>> {
                override fun onLoading() {
                }

                override fun onError(throwable: Throwable) {
                    viewState.onErrorNewsList(throwable)
                }

                override fun onSuccess(elem: List<AboutResponse>) {
                    viewState.onSuccessNewsList(elem)
                }
            }
        )
    }
}