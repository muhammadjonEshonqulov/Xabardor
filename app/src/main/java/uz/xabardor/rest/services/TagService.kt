package uz.xabardor.rest.services

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.xabardor.rest.callbacks.BaseCallback
import uz.xabardor.rest.models.Tag

object TagService : BaseService() {

    fun getMainTags(
        callback: BaseCallback<Tag.ListResponse>
    ) {


        callback.onLoading()
        NewsService.api.getMainTagList(
        ).enqueue(object : Callback<Tag.ListResponse> {
            override fun onFailure(call: Call<Tag.ListResponse>, t: Throwable) {
                callback.onError(t)
            }

            override fun onResponse(call: Call<Tag.ListResponse>, response: Response<Tag.ListResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    callback.onSuccess(response.body()!!)
                } else {
                    callback.onError(Throwable())
                }
            }
        })
    }

}