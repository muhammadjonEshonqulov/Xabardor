package uz.xabardor.rest.models.content

object ContentConstants {
    object Status {
        const val DEFAULT = 0
        const val DOWNLOADING = 1
        const val DOWNLOADED = 2
    }

    object Type {
        const val TEXT = 1
        const val VIDEO = 2
        const val PHOTO = 3
        const val CAPTURE_TEXT = 4
        const val YOUTUBE_VIDEO = 5
    }

}