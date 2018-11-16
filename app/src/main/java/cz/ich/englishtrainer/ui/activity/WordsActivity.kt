package cz.ich.englishtrainer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.adapter.WordsAdapter
import cz.ich.englishtrainer.service.FirestoreService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_albums.*

class WordsActivity : AppCompatActivity() {

    companion object {
        private const val ALBUM_NAME_ARGUMENT = "ALBUM_NAME_ARGUMENT"

        fun getIntent(ctx: Context, albumName: String): Intent {
            val intent = Intent(ctx, WordsActivity::class.java)
            intent.putExtra(ALBUM_NAME_ARGUMENT, albumName)
            return intent
        }
    }

    private val firestoreService = FirestoreService.getInstance(null)
    private lateinit var wordsAdapter: WordsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        initRecycleView()
        loadWords(getIntent().getStringExtra(ALBUM_NAME_ARGUMENT))
    }

    private fun initRecycleView() {
        wordsAdapter = WordsAdapter()

        rec_albums.setHasFixedSize(true)
        rec_albums.layoutManager = LinearLayoutManager(this)
        rec_albums.adapter = wordsAdapter
    }

    private fun loadWords(albumName: String) {
        firestoreService.getWords(albumName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wordsAdapter.setWords(it) }

    }


}