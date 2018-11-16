package cz.ich.englishtrainer.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.adapter.AlbumsAdapter
import cz.ich.englishtrainer.service.FirestoreService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_albums.*
import android.view.MenuItem
import android.content.Intent
import cz.ich.englishtrainer.service.LoginService


class AlbumsActivity : AppCompatActivity() {

    private val firestoreService = FirestoreService.getInstance(null)
    private val loginService: LoginService = LoginService.instance
    private lateinit var albumsAdapter: AlbumsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums)

        initGridView()
        loadAlbums()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_albums, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.getItemId()) {
            R.id.action_add_category -> {

            }
            R.id.action_logout -> loginService.signOut(this) {
                startActivity(Intent(this, StartupActivity::class.java))
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initGridView() {
        albumsAdapter = AlbumsAdapter()

        rec_albums.setHasFixedSize(true)
        rec_albums.layoutManager = GridLayoutManager(this, 2)
        rec_albums.adapter = albumsAdapter
    }

    private fun loadAlbums() {
        firestoreService.albums
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({albumsAdapter.setAlbums(it)})

    }


}