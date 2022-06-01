package uz.xabardor.rest.models.rubric

import java.io.Serializable

data class RubricsData(
    val id: Int?,
    val title: String?,
    val title_cyrl: String?,
    val title_ru: String?,
    val slug: String?,
    val show_on_home: String?,
    val lft: Int?,
    val rght: Int?,
    val tree_id: Int?,
    val level: Int?,
    var expandable: Boolean = false,
    val parent: String?,
    var rubrics: ArrayList<RubricsData>? = null
):Serializable
