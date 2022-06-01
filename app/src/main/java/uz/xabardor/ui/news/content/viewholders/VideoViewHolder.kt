package uz.xabardor.ui.news.content.viewholders

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.downloader.OnDownloadListener
import com.downloader.OnProgressListener
import com.downloader.PRDownloader
import com.downloader.Progress
import kotlinx.coroutines.*
import uz.xabardor.R
import uz.xabardor.extensions.Prefss
import uz.xabardor.extensions.blockClickable
import uz.xabardor.extensions.getFileSize
import uz.xabardor.rest.models.content.Content
import uz.xabardor.ui.base.FileManager
import java.net.URL
import java.util.concurrent.TimeUnit


class VideoViewHolder(val view: View) : BaseViewHolder(view), View.OnClickListener,
    OnDownloadListener, OnProgressListener {
    val prefss = Prefss(itemView.context)
    lateinit var url: String

    lateinit var fileManager: FileManager

    // private var downloadId = 0
    private lateinit var data: Content
    var status: Int = 0

    override fun bindData(data: Content) {
        this.data = data
        val video_img = view.findViewById<ImageView>(R.id.video_img)
        val play = view.findViewById<ImageView>(R.id.play)
        val download = view.findViewById<FrameLayout>(R.id.download)
        val sizeOfVideoContent = view.findViewById<TextView>(R.id.size_of_video_content)


        fileManager = FileManager(itemView.context)

        data.src?.let {
            url = it
            Glide.with(view.context)
                .load("" + it)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(video_img);
        }

        status = if (fileManager.hasOfflineFile(url, "video", true)) {
            download.visibility = View.GONE
            videoLenth()
            2
        } else {
            val fileSize = prefss.get("file${data.src}", 0)
            if (fileSize == 0)
                getFileSizeFromServer("file${data.src}")
            else if (fileSize == -0)
                getFileSizeFromServer("file${data.src}")
            else {
                val width: Float = ((fileSize.toFloat() / 1024) / 1024)
                val text = "%.2f Mb".format(width)
                sizeOfVideoContent.text = text
            }
            download.visibility = View.VISIBLE
            0
        }
        download.setOnClickListener(this)
        play.setOnClickListener(this)

        if (status == 0 && prefss.get(prefss.autoDownload, false))
            download()
    }

    private fun getFileSizeFromServer(key: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            val sizeOfVideoContent = view.findViewById<TextView>(R.id.size_of_video_content)

            var fileSize: Int? = 0
            val job = async { fileSize = URL(url).getFileSize() }
            job.await()
            withContext(Dispatchers.Main) {
                fileSize?.let {
                    prefss.save(key, it)
                    val width: Float = ((it.toFloat() / 1024) / 1024)
                    val text = "%.2f Mb".format(width)
                    sizeOfVideoContent.text = "$text"
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        p0.blockClickable()
        when (status) {
            0 -> download()
            1 -> {
//                if (p0?.id == R.id.play)
//                    return
                cancelDownload()
            }
            2 -> open()
        }
    }

    private fun download() {
        val downloadRequest = PRDownloader.download(
            url, "${fileManager.APP_FILE_DIRECTORY_PATH}video/",
            fileManager.convertUrlToStoragePath(url, true)
        ).build()


        val img = view.findViewById<ImageView>(R.id.img)
        val progressbarVideoContent = view.findViewById<ProgressBar>(R.id.progressbar_video_content)

        downloadRequest.onProgressListener = this
        status = 1
        img.setImageResource(R.drawable.ic_close_24)
        progressbarVideoContent.visibility = View.VISIBLE
        var d = downloadRequest.start(this)
        prefss.save("" + data.src, d)
    }

    private fun videoLenth() {
        val mp = MediaPlayer.create(
            itemView.context,
            Uri.parse(
                fileManager.APP_FILE_DIRECTORY_PATH + "video/" + fileManager.convertUrlToStoragePath(
                    url,
                    true
                )
            )
        )
        try {
            val duration = mp.duration.toLong()
            mp.release()
            val sizeOfVideoContent = view.findViewById<TextView>(R.id.size_of_video_content)

            sizeOfVideoContent.text = "%d:%02d".format(
                TimeUnit.MILLISECONDS.toMinutes(duration),
                TimeUnit.MILLISECONDS.toSeconds(duration) % 60
            )
        } catch (x: Exception) {

        }
    }

    private fun cancelDownload() {
        PRDownloader.pause(prefss.get("" + data.src, 0))
        status = 0
        val img = view.findViewById<ImageView>(R.id.img)
        val progressbarVideoContent = view.findViewById<ProgressBar>(R.id.progressbar_video_content)

        img.setImageResource(R.drawable.ic_downloadsimple)
        progressbarVideoContent.visibility = View.GONE
    }

    private fun open() {
        val data = this.data.copy()
        data.src =
            fileManager.APP_FILE_DIRECTORY_PATH + "video/" + fileManager.convertUrlToStoragePath(
                url,
                true
            )
        listener?.invoke(data)
    }

    override fun onDownloadComplete() {
        status = 2
        val download = view.findViewById<FrameLayout>(R.id.download)

        download.visibility = View.GONE
        videoLenth()
    }

    override fun onError(error: com.downloader.Error?) {
        status = 0
        val img = view.findViewById<ImageView>(R.id.img)
        val progressbarVideoContent = view.findViewById<ImageView>(R.id.progressbar_video_content)

        img.setImageResource(
            R.drawable.ic_downloadsimple
        )
        progressbarVideoContent.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    override fun onProgress(progress: Progress?) {
        progress?.let {
            val progressbarVideoContent = view.findViewById<ProgressBar>(R.id.progressbar_video_content)
            val sizeOfVideoContent = view.findViewById<TextView>(R.id.size_of_video_content)

            val current: Float = ((it.currentBytes / 1024) / 1024F)
            val total: Float = ((it.totalBytes / 1024) / 1024F)
            progressbarVideoContent.progress = (100 * current / total).toInt()
            sizeOfVideoContent.text = "%.2f".format(current) + "/" + "%.2f".format(total) + " Mb"
        }
    }
}