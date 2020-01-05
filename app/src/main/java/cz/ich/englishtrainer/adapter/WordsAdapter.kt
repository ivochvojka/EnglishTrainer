package cz.ich.englishtrainer.adapter

import android.app.Activity
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.widget.TextView
import cz.ich.englishtrainer.R
import cz.ich.englishtrainer.model.Knowledge
import cz.ich.englishtrainer.model.Word
import kotlinx.android.synthetic.main.item_word.view.*
import android.view.*


class WordsAdapter(val parentActivity: Activity) : RecyclerView.Adapter<WordsAdapter.AlbumViewHolder>() {

    private var words: MutableList<Word> = mutableListOf()

    var rowPosition: Int = 0

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

    fun isWordRevealed(): Boolean {
        return words[rowPosition].isRevealed
    }

    fun revealWord() {
        words[rowPosition].isRevealed = true
        notifyItemChanged(rowPosition)
    }

    fun hideWord() {
        words[rowPosition].isRevealed = false
        notifyItemChanged(rowPosition)
    }

    fun updateWord(word: Word) {
        words[rowPosition] = word
        notifyItemChanged(rowPosition)
    }

    fun getWord(): Word {
        return words[rowPosition]
    }

    fun deleteWord() {
        words.removeAt(rowPosition)
        notifyItemRemoved(rowPosition)
    }

    fun hideAllWords() {
        for (word in words) {
            word.isRevealed = false
        }
        notifyDataSetChanged()
    }

    fun revealAllWords() {
        for (word in words) {
            word.isRevealed = true
        }
        notifyDataSetChanged()
    }

    inner class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnCreateContextMenuListener {

        init {
            view.setOnCreateContextMenuListener(this)

            view.setOnLongClickListener {
                rowPosition = adapterPosition
                false
            }
        }

        fun bindView(word: Word) {
            view.layout_word.setBackgroundColor(
                    ContextCompat.getColor(view.context, getColorFromWord(word))
            )

            view.findViewById<TextView>(R.id.txt_lan1).text = word.lan1
            if (word.isRevealed) {
                view.findViewById<TextView>(R.id.txt_lan2).text = word.lan2
            } else {
                view.findViewById<TextView>(R.id.txt_lan2).text = "..."
            }
        }

        override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
            // ICH menu already defined in activity
//            parentActivity.menuInflater.inflate(R.menu.menu_words_context, menu)
//            val popupMenu = PopupMenu(ctx, view)
//            popupMenu.inflate(R.menu.menu_words_context)
//            menu?.add(Menu.NONE, R.id.action_show_translate,
//                    Menu.NONE, R.string.word_add_title);
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