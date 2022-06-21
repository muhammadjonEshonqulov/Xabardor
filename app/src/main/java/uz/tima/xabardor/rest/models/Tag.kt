package uz.tima.xabardor.rest.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Tag(
                    @SerializedName("id")
                    val id: Int = 0,
                    @SerializedName("title")
                    val title: String? = "",
                    @SerializedName("title_cyrl")
                    val title_cyrl: String? = "",
                    @SerializedName("slug")
                    val slug: String = ""): Serializable{


    class ListResponse(
        var data: Data
    ) : Serializable {


        class Data(
                @SerializedName("tags")
            var tags: List<Tag>,
                @SerializedName("pageInfo")
            var pageInfo: PageInfo
        ) : Serializable


        class PageInfo(
            @SerializedName("totalCount")
            var totalCount: Int,
            @SerializedName("nextPage")
            var nextPage: Long
        ) : Serializable
    }
}