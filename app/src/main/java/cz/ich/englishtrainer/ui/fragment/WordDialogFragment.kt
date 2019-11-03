package cz.ich.englishtrainer.ui.fragment

import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import cz.ich.englishtrainer.R
import kotlinx.android.synthetic.main.dialog_add_word.view.*

class WordDialogFragment : DialogFragment() {
    private var mListener: OnWordFilledListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (parentFragment is OnWordFilledListener) {
            mListener = parentFragment as OnWordFilledListener
        } else if (activity is OnWordFilledListener) {
            mListener = activity as OnWordFilledListener
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.dialog_add_word, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState);
        dialog.setTitle(R.string.word_add_title)
        view.edt_english.requestFocus()
        dialog.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        view.btn_save.setOnClickListener {
            mListener?.let {
                it.onWordFilled(view.edt_english.text.toString(),
                        view.edt_czech.text.toString(),
                        view.edt_description.text.toString())
                dismiss()
            }
        }
        view.btn_cancel.setOnClickListener { dismiss() }
    }

    interface OnWordFilledListener {
        fun onWordFilled(en: String, cz: String, description: String?)
    }
}