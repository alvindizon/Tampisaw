package com.alvindizon.tampisaw.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.databinding.FragmentSearchBinding

class SearchFragment: Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}