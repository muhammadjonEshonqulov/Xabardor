package uz.tima.xabardor.rest.callbacks

interface BaseCallback<T> {

    fun onLoading()

    fun onError(throwable: Throwable)

    fun onSuccess(elem: T)

}