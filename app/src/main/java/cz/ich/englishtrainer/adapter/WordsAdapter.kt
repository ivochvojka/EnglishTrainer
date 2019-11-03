package cz.ich.englishtrainer.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.model.Knowledge
import cz.ich.englishtrainer.model.Word
import kotlinx.android.synthetic.main.item_word.view.*

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

    fun addWord(word: Word) {
        this.words.add(word)
        notifyItemInserted(words.size)
    }

    inner class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        fun bindView(word: Word) {
            view.layout_word.setBackgroundColor(
                    ContextCompat.getColor(view.context, getColorFromWord(word))
            )

            view.findViewById<TextView>(R.id.txt_lan1).text = word.lan1
            view.findViewById<TextView>(R.id.txt_lan2).text = word.lan2
        }

        private fun getColorFromWord(word: Word): Int {
            return when (Knowledge.values()[word.kn]) {
                Knowledge.DONT_KNOW -> R.color.knowledge_dont_know
                Knowledge.LESS -> R.color.knowledge_less
                Knowledge.MORE -> R.color.knowledge_more
                Knowledge.REMEMBERED -> R.color.knowledge_remembered
            }
        }
    }
}