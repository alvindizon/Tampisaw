package com.alvindizon.tampisaw.ui.collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.databinding.FragmentCollectionsBinding

class CollectionsFragment: Fragment(R.layout.fragment_collections) {

    private var binding: FragmentCollectionsBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCollectionsBinding.bind(view)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}