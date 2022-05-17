package uz.xabardor.ui.splash

import moxy.InjectViewState
import uz.xabardor.database.TagDatabase
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.rubric.RubricsResponse
import uz.xabardor.rest.services.NewsService
import uz.xabardor.ui.base.BasePresenter

@InjectViewState
class SplashPresenter : BasePresenter<SplashView>() {

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()

        getRubrics()
//        getTags()
    }

    fun getRubrics() {
        NewsService.getRubrics(object : BaseCallback<RubricsResponse> {
            override fun onLoading() {
                viewState.onLoadingTags()
            }

            override fun onError(throwable: Throwable) {
                viewState.onErrorTags(throwable)
            }

            override fun onSuccess(elem: RubricsResponse) {
                elem.data?.rubrics?.let {
                    TagDatabase.mainRubrics = it

                }
                viewState.onSuccessTags()
            }
        })
    }

//    fun getTags() {
//        TagService.getMainTags(object : BaseCallback<Tag.ListResponse> {
//            override fun onLoading() {
//                viewState.onLoadingTags()
//            }
//
//            override fun onError(throwable: Throwable) {
//                viewState.onErrorTags(throwable)
//            }
//
//            override fun onSuccess(elem: Tag.ListResponse) {
//                TagDatabase.mainTags = elem.data.tags
//                viewState.onSuccessTags()
//            }
//        })
//    }

}