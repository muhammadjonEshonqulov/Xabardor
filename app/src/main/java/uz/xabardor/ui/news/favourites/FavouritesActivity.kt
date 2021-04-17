package uz.xabardor.ui.news.favourites

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import moxy.presenter.InjectPresenter
import uz.xabardor.R
import uz.xabardor.extensions.openNewsActivity
import uz.xabardor.extensions.openNewsListActivity
import uz.xabardor.rest.models.Tag
import uz.xabardor.rest.models.news.News
import uz.xabardor.ui.base.BaseActivity
import uz.xabardor.ui.base.recyclerview.OnItemClickListener
import uz.xabardor.ui.main.OnTagClickListener

class FavouritesActivity : BaseActivity(), FavouritesView, OnItemClickListener<News>, OnTagClickListener {


    @InjectPresenter
    lateinit var presenter: FavouritesPresenter

    override val layoutId: Int
        get() = R.layout.activity_favourites


    lateinit var recyclerView: RecyclerView
    lateinit var favouritesAdapter: FavouritesAdapter

    lateinit var emptyTextView: TextView

    override fun setupToolbar() {
        toolBarTitleTextView?.setText(getString(R.string.favourites))
    }

    override fun onCreatedView() {
        recyclerView = findViewById(R.id.recyclerview)
        favouritesAdapter = FavouritesAdapter(recyclerView)
        favouritesAdapter.onItemClickListener = this
        favouritesAdapter.onTagClickListener = this

        emptyTextView = findViewById(R.id.text_view_empty)
    }

    override fun onResume() {
        super.onResume()

        presenter.getFavourites()
    }

    override fun onClick(v: View?) {

    }

    override fun onItemClick(recyclerView: RecyclerView, item: News, position: Int) {
        openNewsActivity(item)
    }

    override fun onTagClick(tag: Tag) {
        openNewsListActivity(tag = tag)
    }

    override fun onSuccessFavourites(list: List<News>) {
        favouritesAdapter.onSuccess(list)

        if(list.isEmpty()){
            emptyTextView.visibility = View.VISIBLE
        }else{
            emptyTextView.visibility = View.INVISIBLE
        }
    }


}