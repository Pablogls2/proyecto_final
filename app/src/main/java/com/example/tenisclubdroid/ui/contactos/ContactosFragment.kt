package com.example.tenisclubdroid.ui.contactos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tenisclubdroid.R

class ContactosFragment : Fragment() {

    private lateinit var contactosViewModel: ContactosViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        contactosViewModel =
                ViewModelProvider(this).get(ContactosViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_contactos, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        contactosViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}