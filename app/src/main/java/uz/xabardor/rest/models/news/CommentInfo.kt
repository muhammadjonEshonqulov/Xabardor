package uz.xabardor.rest.models.news

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommentInfo(@SerializedName("allow")
                       val allow: Boolean = false,
                       @SerializedName("show")
                       val show: Boolean = false,
                       @SerializedName("count")
                       val count: Int = 0): Serializable