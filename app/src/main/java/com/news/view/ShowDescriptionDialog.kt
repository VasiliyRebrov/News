package com.news.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.news.R
import kotlinx.android.synthetic.main.dialog_show_description.*

class ShowDescriptionDialog : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_show_description, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        text_dialog_description.text = requireArguments().getString(STRING_KEY)!!
    }

    companion object {
        private const val STRING_KEY = "description"

        @JvmStatic
        fun newInstance(description: String) =
            ShowDescriptionDialog().apply {
                arguments = Bundle().apply {
                    putString(STRING_KEY, description)
                }
            }
    }
}