package cz.ich.englishtrainer.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.model.Word

class WordsAdapter : RecyclerView.Adapter<WordsAdapter.AlbumViewHolder>() {

    private var words: MutableList<Word> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_word, parent,
                        false))
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bindView(words[position])
    }

    override fun getItemCount(): Int {
        return words.size
    }

    fun setWords(words: List<Word>) {
        this.words.clear()
        this.words.addAll(words)
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(word: Word) {
            view.findViewById<TextView>(R.id.txt_lan1).text = word.lan1
            view.findViewById<TextView>(R.id.txt_lan2).text = word.lan2
        }
    }
}