package com.dam.musicapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.musicapp.R
import com.dam.musicapp.model.Rehab
import com.dam.musicapp.ui.adapter.RehabAdapter

class RehabFragment : Fragment() {
    private val rehabs = Rehab.getRehabs()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val view = inflater.inflate(R.layout.fragment_rehab, container, false)
        val recyclerRehab = view.findViewById<RecyclerView>(R.id.recyclerRehab)

        recyclerRehab.layoutManager = LinearLayoutManager(requireContext())
        recyclerRehab.adapter = RehabAdapter(rehabs)
        return view
    }

    companion object {
        fun newInstance() :
        RehabFragment =  RehabFragment()
    }
}