package com.dam.rehapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dam.rehapp.R
import com.dam.rehapp.model.NivelRehab
import com.dam.rehapp.model.Rehab
import com.dam.rehapp.ui.adapter.NivelAdapter


class NivelRehabFragment : Fragment() {

    private lateinit var recyclerNiveles: RecyclerView
    private lateinit var niveles: List<NivelRehab>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_nivel_rehab, container, false)

        recyclerNiveles = view.findViewById(R.id.recyclerNiveles)

        val rehabId = arguments?.getInt("rehabId") ?: 0
        niveles = NivelRehab.getNiveles().filter { it.rehabId == rehabId }

        recyclerNiveles.layoutManager = LinearLayoutManager(requireContext())
        recyclerNiveles.adapter = NivelAdapter(niveles)



        return view
    }

    companion object {
        fun newInstance(rehab: Rehab): NivelRehabFragment {
            val fragment = NivelRehabFragment()
            val bundle = Bundle()
            bundle.putInt("rehabId", rehab.id)
            fragment.arguments = bundle
            return fragment
        }
    }
}