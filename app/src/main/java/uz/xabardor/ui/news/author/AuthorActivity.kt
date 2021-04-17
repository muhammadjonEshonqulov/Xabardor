package uz.xabardor.ui.news.author

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import moxy.presenter.InjectPresenter
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView
import moxy.presenter.ProvidePresenter
import uz.xabardor.R
import uz.xabardor.extensions.openNewsActivity
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.Author
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnItemClickListener

class AuthorActivity : BaseActivity(), AuthorView, OnItemClickListener<Tag> {

    @InjectPresenter
    lateinit var presenter: AuthorPresenter

    @ProvidePresenter
    fun providerPresenter() = AuthorPresenter().apply {
        author = this@AuthorActivity.author
    }

    override val layoutId: Int
        get() = R.layout.activity_author

    lateinit var authorNameTextView: TextView
    lateinit var authorDescriptionTextView: TextView
    lateinit var authorImageView: CircleImageView

    lateinit var tagsRecyclerView: RecyclerView
    lateinit var tagsAdapter: AuthorTagsAdapter

    lateinit var newsRecyclerView: RecyclerView
    lateinit var newsListAdapter: AuthorNewsAdapter

    lateinit var emptyTextView: TextView

    override fun setupToolbar() {

    }

    override fun onCreatedView() {
        emptyTextView = findViewById(R.id.text_view_empty)
        authorNameTextView = findViewById(R.id.text_view_author_name)
        authorDescriptionTextView = findViewById(R.id.text_view_author_description)
        authorImageView = findViewById(R.id.image_view_author)


        tagsRecyclerView = findViewById(R.id.recyclerview_tags)
        tagsAdapter = AuthorTagsAdapter(tagsRecyclerView)
        tagsAdapter.onItemClickListener = this

        newsRecyclerView = findViewById(R.id.recyclerview)
        newsRecyclerView.isNestedScrollingEnabled = false
        newsRecyclerView.setHasFixedSize(false)
        newsListAdapter = AuthorNewsAdapter(newsRecyclerView)
        newsListAdapter.onItemClickListener = object : OnItemClickListener<News> {
            override fun onItemClick(recyclerView: RecyclerView, item: News, position: Int) {
                openNewsActivity(item)
            }
        }

        author.also {
            author
            Glide.with(this)
                    .load(it.avatar)
                    .into(authorImageView)


            authorNameTextView.setText(it.fullName)
            authorDescriptionTextView.setText(it.description)
        }
    }

    override fun onClick(v: View?) {

    }

    override fun onItemClick(recyclerView: RecyclerView, item: Tag, position: Int) {
        if (position == 0)
            presenter.selectedTag = null
        else
            presenter.selectedTag = item
        tagsAdapter.selectedTag = item
        tagsAdapter.notifyDataSetChanged()

        newsListAdapter.onSuccess(listOf())

        presenter.refresh()
    }

    override fun onLoadingAuthorInfo() {

    }

    override fun onErrorAuthorInfo(throwable: Throwable) {

    }

    override fun onSuccessAuthorInfo() {
        tagsAdapter.selectedTag = presenter.tags.firstOrNull()
        tagsAdapter.onSuccess(presenter.tags)
    }

    override fun onLoadingAuthorNewsList() {

    }

    override fun onErrorAuthorNewsList(throwable: Throwable) {
        emptyTextView.visibility = View.VISIBLE
    }

    override fun onSuccessAuthorNewsList() {
        newsListAdapter.onSuccess(presenter.newsList)


        if (presenter.newsList.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
        } else {
            emptyTextView.visibility = View.INVISIBLE
        }
    }

    val author: Author get() = intent.getSerializableExtra(BUNDLE_AUTHOR) as Author

    companion object {
        val BUNDLE_AUTHOR = "author"
    }

}