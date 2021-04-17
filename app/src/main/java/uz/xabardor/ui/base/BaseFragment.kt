package uz.xabardor.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import moxy.MvpAppCompatFragment

abstract class BaseFragment: MvpAppCompatFragment(), View.OnClickListener {

    abstract val layoutId: Int get

    abstract fun onCreatedView(view: View)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutId , container , false)
        onCreatedView(view)

        return view
    }

}