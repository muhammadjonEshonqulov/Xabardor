package uz.tima.xabardor.ui.about

import moxy.InjectViewState
import uz.tima.xabardor.rest.callbacks.BaseCallback
import uz.tima.xabardor.rest.models.AboutResponse
import uz.tima.xabardor.rest.services.NewsService
import uz.tima.xabardor.ui.base.BasePresenter

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