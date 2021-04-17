package uz.xabardor.rest.models.news

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Image(@SerializedName("showList")
                 val showInList: Boolean = false,
                 @SerializedName("thumb")
                 val thumb: String = "",
                 @SerializedName("show")
                 val show: Boolean = false,
                 @SerializedName("caption")
                 val caption: String = "",
                 @SerializedName("orginal")
                 val orginal: String = ""): Serializable