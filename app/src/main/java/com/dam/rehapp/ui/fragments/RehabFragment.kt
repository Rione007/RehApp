package com.dam.rehapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.rehapp.R
import com.dam.rehapp.model.Rehab
import com.dam.rehapp.ui.adapter.RehabAdapter

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
        recyclerRehab.adapter = RehabAdapter(rehabs) { selectedRehab ->
            openNivelRehabFragment(selectedRehab)
        }
        return view
    }
    private fun openNivelRehabFragment(rehab: Rehab) {
        val fragment = NivelRehabFragment.newInstance(rehab)

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        fun newInstance() :
        RehabFragment =  RehabFragment()
    }
}