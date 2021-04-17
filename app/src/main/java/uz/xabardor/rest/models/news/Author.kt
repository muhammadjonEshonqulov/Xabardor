package uz.xabardor.rest.models.news

import com.google.gson.annotations.SerializedName
import uz.xabardor.rest.models.Tag
import java.io.Serializable

data class Author(@SerializedName("hidden")
                  val hidden: Boolean = false,
                  @SerializedName("nickname")
                  val nickName: String = "",
                  @SerializedName("fullName")
                  val fullName: String = "",
                  @SerializedName("description")
                  val description: String = "",
                  @SerializedName("avatar")
                  val avatar: String = "") : Serializable {


    class DetailResponse(
            var data: Data
    ) : Serializable {


        class Data(
                @SerializedName("author")
                var author: Author,
                @SerializedName("tags")
                var tags: List<Tag>
        ) : Serializable
    }

}