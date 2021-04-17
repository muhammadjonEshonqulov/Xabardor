package uz.xabardor.ui.splash

import moxy.InjectViewState
import uz.xabardor.database.TagDatabase
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.services.TagService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class SplashPresenter : BasePresenter<SplashView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getTags()
    }

    fun getTags() {
        TagService.getMainTags(object : BaseCallback<Tag.ListResponse> {
            override fun onLoading() {
                viewState.onLoadingTags()
            }

            override fun onError(throwable: Throwable) {
                viewState.onErrorTags(throwable)
            }

            override fun onSuccess(elem: Tag.ListResponse) {
                TagDatabase.mainTags = elem.data.tags
                viewState.onSuccessTags()
            }
        })
    }

}