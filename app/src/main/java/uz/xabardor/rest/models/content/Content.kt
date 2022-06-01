package uz.xabardor.rest.models.content

data class Content(
    val type : String, // "text",  "break",  "unknown",  "image", "link",
    var src : String?,
    val text : String?,
    val caption : String?,
    val href : String?,
)