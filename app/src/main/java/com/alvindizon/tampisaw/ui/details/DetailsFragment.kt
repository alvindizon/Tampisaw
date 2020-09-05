package com.alvindizon.tampisaw.ui.details

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.core.ui.DialogManager
import com.alvindizon.tampisaw.databinding.FragmentDetailsBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import javax.inject.Inject

class DetailsFragment: Fragment(R.layout.fragment_details) {

    private var binding: FragmentDetailsBinding? = null

    private lateinit var viewModel: DetailsViewModel

    private val args: DetailsFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var dialogManager: DialogManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.uiState?.observe(this, {
            binding?.progressBar?.isVisible = it is LOADING
            binding?.image?.isVisible = it is SUCCESS
            if (it is SUCCESS) {
                binding?.image?.let { imgView ->
                    Glide.with(requireContext())
                        .load(it.photoUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(imgView)
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,callback)

        viewLifecycleOwner.lifecycle.addObserver(viewModel)

        viewModel.getPhoto(args.url)

        binding?.apply {
            fab.setOnClickListener {
                dialogManager.showDialog(InfoBottomSheet.newInstance())
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}
