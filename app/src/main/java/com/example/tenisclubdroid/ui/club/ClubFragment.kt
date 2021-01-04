package com.example.tenisclubdroid.ui.club

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tenisclubdroid.R

class ClubFragment : Fragment() {

    private lateinit var clubViewModel: ClubViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        clubViewModel =
                ViewModelProvider(this).get(ClubViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_club, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        clubViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}