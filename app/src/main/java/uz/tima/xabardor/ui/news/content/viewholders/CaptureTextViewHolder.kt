package uz.tima.xabardor.ui.news.content.viewholders

import android.os.Build
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import uz.tima.xabardor.R
import uz.tima.xabardor.extensions.language.Language
import uz.tima.xabardor.rest.models.content.Content


class CaptureTextViewHolder(val view: View) :
    BaseViewHolder(view) {

    var htmlData = ""

    override fun bindData(data: Content) {
        val textContent = view.findViewById<TextView>(R.id.text_content)
        if (data.type == "image-caption"){
            data.src?.let {
                htmlData = it
            }
        }
//        htmlData = htmlData.replace("<a", "<a style=\"text-decoration: none;\"", true)

        itemView.context?.let {
            if (htmlData.isNotEmpty())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    textContent.text = Html.fromHtml(htmlData,Html.FROM_HTML_MODE_COMPACT)
                } else {
                    textContent.text = Html.fromHtml(htmlData)
                }
            textContent.movementMethod = LinkMovementMethod.getInstance()

        }
    }
}