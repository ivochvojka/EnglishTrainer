package cz.ich.englishtrainer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.ui.activity.WordsActivity

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    private var albums: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent,
                        false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindView(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    fun setAlbums(albums: List<String>) {
        this.albums.clear()
        this.albums.addAll(albums)
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(albumName: String) {
            view.findViewById<ViewGroup>(R.id.container_album).setOnClickListener {
                val intent = WordsActivity.getIntent(it.context, albumName)
                it.context.startActivity(intent)
            }
            view.findViewById<TextView>(R.id.txt_album).text = albumName
        }
    }
}