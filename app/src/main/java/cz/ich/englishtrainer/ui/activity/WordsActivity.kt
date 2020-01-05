package cz.ich.englishtrainer.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.adapter.WordsAdapter
import cz.ich.englishtrainer.model.Knowledge
import cz.ich.englishtrainer.model.Word
import cz.ich.englishtrainer.service.FirestoreService
import cz.ich.englishtrainer.ui.fragment.WordDialogFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_albums.*

/**
 * FIXME error handling
 */
class WordsActivity : AppCompatActivity(), WordDialogFragment.OnWordFilledListener {
    companion object {
        private val TAG = WordsActivity::class.java.name
        private const val ALBUM_NAME_ARGUMENT = "ALBUM_NAME_ARGUMENT"

        fun getIntent(ctx: Context, albumName: String): Intent {
            val intent = Intent(ctx, WordsActivity::class.java)
            intent.putExtra(ALBUM_NAME_ARGUMENT, albumName)
            return intent
        }
    }

    private val firestoreService = FirestoreService.getInstance(null)
    private lateinit var wordsAdapter: WordsAdapter
    private lateinit var album: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        album = intent.getStringExtra(ALBUM_NAME_ARGUMENT)

        initRecycleView()
        loadWords(getIntent().getStringExtra(ALBUM_NAME_ARGUMENT))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_words, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_add_word -> createNewWord()
            R.id.action_show_translate_all -> revealAllWords()
            R.id.action_hide_translate_all -> hideAllWords()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menuInflater.inflate(R.menu.menu_words_context, menu)

        val showTranslationItem = menu?.findItem(R.id.action_show_translate)
        val hideTranslationItem = menu?.findItem(R.id.action_hide_translate)
        if (wordsAdapter.isWordRevealed()) {
            showTranslationItem?.isVisible = false
            hideTranslationItem?.isVisible = true
        } else {
            showTranslationItem?.isVisible = true
            hideTranslationItem?.isVisible = false
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "in onContextItemSelected, position=${wordsAdapter.rowPosition}")
        when (item.itemId) {
            R.id.action_show_translate -> revealWord()
            R.id.action_hide_translate -> hideWord()
            R.id.action_edit -> editItem()
            R.id.action_delete -> deleteItem()
            R.id.action_difficulty_lower -> changeDifficulty(1)
            R.id.action_difficulty_higher -> changeDifficulty(-1)
        }

        return true
    }

    private fun hideAllWords() {
        wordsAdapter.hideAllWords()
    }

    private fun revealAllWords() {
        wordsAdapter.revealAllWords()
    }

    private fun revealWord() {
        wordsAdapter.revealWord()
    }

    private fun hideWord() {
        wordsAdapter.hideWord()
    }

    private fun changeDifficulty(difficultyStep: Int) {
        val word = wordsAdapter.getWord()
        if (difficultyStep == 1 && word.kn < Knowledge.REMEMBERED.ordinal) {
            word.kn++
            firestoreService.updateWord(word)
        } else if (difficultyStep == -1 && word.kn > Knowledge.DONT_KNOW.ordinal) {
            word.kn--
            firestoreService.updateWord(word)
        }
        wordsAdapter.updateWord(word)
    }

    private fun deleteItem() {
        firestoreService.deleteWord(wordsAdapter.getWord())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ wordsAdapter.deleteWord() }, {})
    }

    private fun editItem() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onWordFilled(en: String, cz: String, description: String?) {
        val word = Word(en, cz, description)
        firestoreService.addWord(album, word)
        wordsAdapter.addWord(word)
    }

    private fun initRecycleView() {
        wordsAdapter = WordsAdapter(this)

        rec_albums.setHasFixedSize(true)
        rec_albums.layoutManager = LinearLayoutManager(this)
        rec_albums.adapter = wordsAdapter
        registerForContextMenu(rec_albums)
    }

    private fun loadWords(albumName: String) {
        firestoreService.getWords(albumName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { wordsAdapter.setWords(it) }

    }

    private fun createNewWord() {
        val dialogFragment = WordDialogFragment()
        dialogFragment.show(fragmentManager, "dialog")
    }


}
