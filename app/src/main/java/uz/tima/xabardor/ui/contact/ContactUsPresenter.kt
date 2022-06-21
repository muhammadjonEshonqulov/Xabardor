package uz.tima.xabardor.ui.contact

import moxy.InjectViewState
import uz.tima.xabardor.rest.callbacks.BaseCallback
import uz.tima.xabardor.rest.models.AboutResponse
import uz.tima.xabardor.rest.services.NewsService
import uz.tima.xabardor.ui.base.BasePresenter

@InjectViewState
class ContactUsPresenter : BasePresenter<ContactUsView>() {

    fun getContact() {
        NewsService.getContact(
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