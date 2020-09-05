package com.alvindizon.tampisaw.ui.details

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.alvindizon.tampisaw.R
import com.alvindizon.tampisaw.core.ViewModelFactory
import com.alvindizon.tampisaw.databinding.DialogPhotoDetailsBinding
import com.alvindizon.tampisaw.di.InjectorUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import javax.inject.Inject

class InfoBottomSheet : BottomSheetDialogFragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: DetailsViewModel

    private var binding: DialogPhotoDetailsBinding? =null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        InjectorUtils.getPresentationComponent(requireActivity()).inject(this)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DetailsViewModel::class.java)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheetDialog = super.onCreateDialog(savedInstanceState)

        bottomSheetDialog.setOnShowListener {
            val bottomSheet = bottomSheetDialog
                .findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            BottomSheetBehavior.from(bottomSheet).apply {
                skipCollapsed = true
                state = BottomSheetBehavior.STATE_EXPANDED
            }
        }

        return bottomSheetDialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_photo_details, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.apply {
            viewModel = this@InfoBottomSheet.viewModel
            lifecycleOwner = this@InfoBottomSheet
            executePendingBindings()
        }
    }

    companion object {
        val TAG = InfoBottomSheet::class.java.simpleName
        fun newInstance() = InfoBottomSheet()
    }
}